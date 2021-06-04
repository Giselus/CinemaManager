DROP SEQUENCE IF EXISTS idSeq CASCADE;
CREATE SEQUENCE idSeq start 1;

CREATE OR REPLACE FUNCTION klient_ins() RETURNS TRIGGER AS $$
BEGIN
    NEW.id = nextval('idSeq');
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

CREATE OR REPLACE FUNCTION score(_id int) RETURNS numeric(3,2) AS $$
DECLARE
    ilosc int;
    p numeric(3,2);
    f record;
BEGIN
    ilosc = 0;
    p = 0;
    FOR f IN (SELECT id_klienta, ocena FROM film JOIN historia_cen ON id_filmu = id
      WHERE id = _id AND (id_klienta,data_wystawienia)
      IN (SELECT id_klienta, MAX(data_wystawienia) FROM historia_cen GROUP BY(id_klienta))) LOOP
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

CREATE OR REPLACE VIEW film_filtry AS SELECT id, score(id) as score, tytul FROM film;