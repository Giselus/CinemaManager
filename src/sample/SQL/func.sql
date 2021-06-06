DROP SEQUENCE IF EXISTS idSeq CASCADE;
CREATE SEQUENCE idSeq start 1;

CREATE OR REPLACE FUNCTION klient_ins() RETURNS TRIGGER AS $$
BEGIN
    IF NEW.id IS NULL THEN
        NEW.id = nextval('idSeq');
    END IF;
    RETURN NEW;
END;
$$ language plpgsql;

CREATE OR REPLACE FUNCTION klient_upd() RETURNS TRIGGER AS $$
BEGIN
    NEW.id = OLD.id;
    RETURN NEW;
END;
$$ language plpgsql;

DROP TRIGGER IF EXISTS klient_ins ON klient CASCADE;
DROP TRIGGER IF EXISTS klient_upd ON klient CASCADE;
CREATE TRIGGER klient_ins BEFORE INSERT ON klient FOR EACH ROW EXECUTE PROCEDURE klient_ins();
CREATE TRIGGER klient_upd BEFORE UPDATE ON klient FOR EACH ROW EXECUTE PROCEDURE klient_upd();

DROP TRIGGER IF EXISTS gatunek_ins ON gatunek CASCADE;
DROP TRIGGER IF EXISTS gatunek_upd ON gatunek CASCADE;
CREATE TRIGGER gatunek_ins BEFORE INSERT ON gatunek FOR EACH ROW EXECUTE PROCEDURE klient_ins();
CREATE TRIGGER gatunek_upd BEFORE UPDATE ON gatunek FOR EACH ROW EXECUTE PROCEDURE klient_upd();

DROP TRIGGER IF EXISTS jezyk_ins ON jezyk CASCADE;
DROP TRIGGER IF EXISTS jezyk_upd ON jezyk CASCADE;
CREATE TRIGGER jezyk_ins BEFORE INSERT ON jezyk FOR EACH ROW EXECUTE PROCEDURE klient_ins();
CREATE TRIGGER jezyk_upd BEFORE UPDATE ON jezyk FOR EACH ROW EXECUTE PROCEDURE klient_upd();

DROP TRIGGER IF EXISTS wytwornia_ins ON wytwornia CASCADE;
DROP TRIGGER IF EXISTS wytwornia_upd ON wytwornia CASCADE;
CREATE TRIGGER wytwornia_ins BEFORE INSERT ON wytwornia FOR EACH ROW EXECUTE PROCEDURE klient_ins();
CREATE TRIGGER wytwornia_upd BEFORE UPDATE ON wytwornia FOR EACH ROW EXECUTE PROCEDURE klient_upd();

DROP TRIGGER IF EXISTS kraj_ins ON kraj CASCADE;
DROP TRIGGER IF EXISTS kraj_upd ON kraj CASCADE;
CREATE TRIGGER kraj_ins BEFORE INSERT ON kraj FOR EACH ROW EXECUTE PROCEDURE klient_ins();
CREATE TRIGGER kraj_upd BEFORE UPDATE ON kraj FOR EACH ROW EXECUTE PROCEDURE klient_upd();

DROP TRIGGER IF EXISTS osoby_ins ON osoby CASCADE;
DROP TRIGGER IF EXISTS osoby_upd ON osoby CASCADE;
CREATE TRIGGER osoby_ins BEFORE INSERT ON osoby FOR EACH ROW EXECUTE PROCEDURE klient_ins();
CREATE TRIGGER osoby_upd BEFORE UPDATE ON osoby FOR EACH ROW EXECUTE PROCEDURE klient_upd();

DROP TRIGGER IF EXISTS pozycja_ins ON pozycja CASCADE;
DROP TRIGGER IF EXISTS pozycja_upd ON pozycja CASCADE;
CREATE TRIGGER pozycja_ins BEFORE INSERT ON pozycja FOR EACH ROW EXECUTE PROCEDURE klient_ins();
CREATE TRIGGER pozycja_upd BEFORE UPDATE ON pozycja FOR EACH ROW EXECUTE PROCEDURE klient_upd();

DROP TRIGGER IF EXISTS film_ins ON film CASCADE;
DROP TRIGGER IF EXISTS film_upd ON film CASCADE;
CREATE TRIGGER film_ins BEFORE INSERT ON film FOR EACH ROW EXECUTE PROCEDURE klient_ins();
CREATE TRIGGER film_upd BEFORE UPDATE ON film FOR EACH ROW EXECUTE PROCEDURE klient_upd();

DROP TRIGGER IF EXISTS seans_ins ON seans CASCADE;
DROP TRIGGER IF EXISTS seans_upd ON seans CASCADE;
CREATE TRIGGER seans_ins BEFORE INSERT ON seans FOR EACH ROW EXECUTE PROCEDURE klient_ins();
CREATE TRIGGER seans_upd BEFORE UPDATE ON seans FOR EACH ROW EXECUTE PROCEDURE klient_upd();

DROP TRIGGER IF EXISTS zamowienie_ins ON zamowienie CASCADE;
DROP TRIGGER IF EXISTS zamowienie_upd ON zamowienie CASCADE;
CREATE TRIGGER zamowienie_ins BEFORE INSERT ON zamowienie FOR EACH ROW EXECUTE PROCEDURE klient_ins();
CREATE TRIGGER zamowienie_upd BEFORE UPDATE ON zamowienie FOR EACH ROW EXECUTE PROCEDURE klient_upd();

DROP TRIGGER IF EXISTS sala_ins ON sala CASCADE;
DROP TRIGGER IF EXISTS sala_upd ON sala CASCADE;
CREATE TRIGGER sala_ins BEFORE INSERT ON sala FOR EACH ROW EXECUTE PROCEDURE klient_ins();
CREATE TRIGGER sala_upd BEFORE UPDATE ON sala FOR EACH ROW EXECUTE PROCEDURE klient_upd();

CREATE OR REPLACE FUNCTION znizka_ins() RETURNS TRIGGER AS $$
BEGIN
    IF NEW.id_znizki IS NULL THEN
        NEW.id_znizki = nextval('idSeq');
    END IF;
    RETURN NEW;
END;
$$ language plpgsql;

CREATE OR REPLACE FUNCTION znizka_upd() RETURNS TRIGGER AS $$
BEGIN
    NEW.id_znizki = OLD.id_znizki;
    RETURN NEW;
END;
$$ language plpgsql;

DROP TRIGGER IF EXISTS znizka_ins ON znizka CASCADE;
DROP TRIGGER IF EXISTS znizka_upd ON znizka CASCADE;
CREATE TRIGGER znizka_ins BEFORE INSERT ON znizka FOR EACH ROW EXECUTE PROCEDURE znizka_ins();
CREATE TRIGGER znizka_upd BEFORE UPDATE ON znizka FOR EACH ROW EXECUTE PROCEDURE znizka_upd();

CREATE OR REPLACE FUNCTION score(_id int) RETURNS numeric AS $$
DECLARE
    ilosc int;
    p numeric;
    f record;
BEGIN
    ilosc = 0;
    p = 0;
    FOR f IN (SELECT id_klienta, ocena FROM film JOIN historia_ocen ON id_filmu = id
      WHERE id = _id AND (id_klienta,data_wystawienia)
      IN (SELECT id_klienta, MAX(data_wystawienia) FROM historia_ocen GROUP BY(id_klienta))) LOOP
        p = p + f.ocena;
        ilosc = ilosc + 1;
    END LOOP;
    IF ilosc = 0 THEN
        RETURN 5;
    END IF;
    p = p/ilosc;
    RETURN p;
END;
$$ language plpgsql;



CREATE OR REPLACE VIEW film_filtry AS SELECT f.*, score(id) as score FROM film f;

CREATE OR REPLACE FUNCTION cena_biletu(_id_seansu int, _id_znizki int) RETURNS numeric AS $$
DECLARE
    p numeric;
    f record;
    cena_bazowa numeric;
    procent numeric;
BEGIN
    cena_bazowa = (SELECT cena FROM seans WHERE id = _id_seansu);
    procent = (SELECT procent_znizki FROM znizka WHERE id_znizki = _id_znizki);
    p = cena_bazowa * (100-procent);
    RETURN p;
END;
$$ language plpgsql;


CREATE OR REPLACE VIEW bilet_cena AS SELECT b.*, cena_biletu(id_seansu, id_znizki) as cena FROM bilet b;

CREATE OR REPLACE FUNCTION cena_zamowienia(_id int) RETURNS numeric AS $$
DECLARE
    p numeric;
    f record;
BEGIN
    FOR f in (SELECT * FROM bilet_cena WHERE id_zamowienia = _id) LOOP
        p = p + f.cena;
    END LOOP;
    RETURN p;
END;
$$ language plpgsql;


CREATE OR REPLACE VIEW zamowienie_cena AS SELECT z.*, cena_zamowienia(id) as cena FROM zamowienie z;

CREATE OR REPLACE FUNCTION dodaj_osobe(id1 int, id2 int, nazwa1 varchar(20)) RETURNS void AS $$
DECLARE
    id_pozycji int;
BEGIN
    IF EXISTS(SELECT * FROM pozycja WHERE nazwa = nazwa1) THEN
        id_pozycji = (SELECT id FROM pozycja WHERE nazwa = nazwa1);
    ELSE
        INSERT INTO pozycja VALUES(NULL, nazwa1);
        id_pozycji = (SELECT id FROM pozycja WHERE nazwa = nazwa1);
    END IF;
    INSERT INTO produkcja VALUES(id1,id2,id_pozycji);
END;
$$ language plpgsql;