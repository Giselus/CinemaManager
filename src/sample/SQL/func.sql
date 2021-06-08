DROP SEQUENCE IF EXISTS idSeq CASCADE;
CREATE SEQUENCE idSeq start 10000;

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

CREATE OR REPLACE FUNCTION cena_biletu(_id_zamowienia int, _id_znizki int) RETURNS numeric AS $$
DECLARE
    p numeric;
    f record;
    cena_bazowa numeric;
    procent numeric;
BEGIN
    cena_bazowa = (SELECT cena FROM zamowienie z JOIN seans s ON s.id = id_seansu WHERE z.id = _id_zamowienia);
    procent = (SELECT procent_znizki FROM znizka WHERE id_znizki = _id_znizki);
    p = cena_bazowa * (100-procent);
    RETURN p;
END;
$$ language plpgsql;


CREATE OR REPLACE VIEW bilet_cena AS SELECT b.*, cena_biletu(id_zamowienia, id_znizki) as cena FROM bilet b;

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

CREATE OR REPLACE FUNCTION czysc_zamowienia() RETURNS void AS $$
DECLARE
    f record;
BEGIN
    FOR f IN (SELECT * FROM zamowienie z JOIN seans s ON s.id = id_seansu
    WHERE data_rozpoczecia > current_date AND data_rozpoczecia <= current_date + interval '30 minute'
    AND zrealizowane = false) LOOP
        DELETE FROM zamowienie WHERE id = f.z.id;
    END LOOP;
END;
$$ language plpgsql;

CREATE OR REPLACE FUNCTION historia_data_check() RETURNS TRIGGER AS $$
BEGIN
    IF OLD IS NULL THEN
        NEW.data_wystawienia = current_date;
        RETURN NEW;
    END IF;
   NEW.data_wystawienia = OLD.data_wystawienia;
   RETURN NEW;
END;
$$ language plpgsql;

DROP TRIGGER IF EXISTS historia_data_check ON historia_ocen;
CREATE TRIGGER historia_data_check BEFORE INSERT OR UPDATE ON historia_ocen FOR EACH ROW EXECUTE PROCEDURE historia_data_check();

CREATE OR REPLACE FUNCTION zamowienie_data_check() RETURNS TRIGGER AS $$
BEGIN
    IF OLD != NULL THEN
        NEW.data_zamowienia = OLD.data_zamowienia;
        RETURN NEW;
    END IF;
    NEW.data_zamowienia = current_date;
    RETURN NEW;
END;
$$ language plpgsql;

DROP TRIGGER IF EXISTS zamowienie_data_check ON zamowienie;
CREATE TRIGGER zamowienie_data_check BEFORE INSERT OR UPDATE ON zamowienie FOR EACH ROW EXECUTE PROCEDURE zamowienie_data_check();

CREATE OR REPLACE FUNCTION wolne_miejsca(_id int) RETURNS int AS $$
DECLARE
    sal record;
    iter record;
    zajete int;
    rzedy int;
    kolumnys int;
BEGIN
    zajete = 0;
    FOR iter IN (SELECT * FROM bilet b JOIN zamowienie z ON b.id_zamowienia = z.id
     JOIN seans s ON z.id_seansu = s.id WHERE s.id = _id) LOOP
        zajete = zajete + 1;
     END LOOP;
     rzedy = (SELECT liczba_rzedow FROM seans s JOIN sala sa ON s.id_sala = sa.id WHERE s.id = _id LIMIT 1);
     kolumnys = (SELECT miejsca_w_rzedzie FROM seans s JOIN sala sa ON s.id_sala = sa.id WHERE s.id = _id LIMIT 1);
     RETURN rzedy * kolumnys - zajete;
END;
$$ language plpgsql;

DROP VIEW IF EXISTS seans_miejsca;
CREATE OR REPLACE VIEW seans_miejsca as SELECT z.*, wolne_miejsca(z.id) as wolne_miejsca FROM seans z;

DROP VIEW IF EXISTS film_komentarze;
CREATE OR REPLACE VIEW film_komentarze AS (SELECT f.id,h.ocena, h.komentarz, h.data_wystawienia, k.imie, k.nazwisko
FROM film f JOIN historia_ocen h ON f.id = id_filmu
 JOIN klient k ON k.id = id_klienta ORDER BY data_wystawienia);

DROP VIEW IF EXISTS role_osoby;
CREATE OR REPLACE VIEW role_osoby AS (SELECT o.id,p.nazwa,f.id as id_filmu, f.tytul, f.data_premiery
FROM produkcja JOIN osoby o ON id_osoba = o.id JOIN
pozycja p ON p.id = id_pozycja JOIN film f ON f.id = id_filmu);