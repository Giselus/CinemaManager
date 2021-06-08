CREATE TYPE dzwiek_type AS ENUM ('oryginal', 'lektor', 'dubbing');
CREATE TYPE plec AS ENUM ('kobieta', 'mezczyzna');
CREATE  TABLE film (
	id                   integer  NOT NULL ,
	tytul                varchar(200)  NOT NULL ,
	data_premiery        date   ,
	czas_trwania         integer  NOT NULL ,
	CONSTRAINT unq_filmy_id UNIQUE ( id ) ,
	CONSTRAINT pk_filmy_id PRIMARY KEY ( id )
 );

CREATE  TABLE gatunek ( 
	id                   integer  NOT NULL ,
	rodzaj               varchar(20)  NOT NULL ,
	CONSTRAINT pk_gatunek_id PRIMARY KEY ( id ),
	CONSTRAINT unq_gatunek_name UNIQUE ( rodzaj )
 );

CREATE  TABLE jezyk ( 
	id                   integer  NOT NULL ,
	jezyk_oryginalu      varchar(20)  NOT NULL ,
	CONSTRAINT pk_jezyk_id PRIMARY KEY ( id ),
	CONSTRAINT unq_jezyk UNIQUE ( jezyk_oryginalu )
 );

CREATE  TABLE klient ( 
	id                   integer  NOT NULL ,
	imie                 varchar(20)  NOT NULL ,
	nazwisko             varchar(20)  NOT NULL ,
	login                varchar(15)  NOT NULL ,
	haslo                varchar(15)   ,
	CONSTRAINT pk_klient_id PRIMARY KEY ( id ),
	CONSTRAINT unq_login_name UNIQUE ( login )
 );

CREATE  TABLE kraj ( 
	id                   integer  NOT NULL ,
	kraj                 varchar  NOT NULL ,
	CONSTRAINT pk_kraj_id PRIMARY KEY ( id ),
	CONSTRAINT unq_kraj_name UNIQUE ( kraj )
 );

CREATE  TABLE osoby ( 
	id                   integer  NOT NULL ,
	imie                 varchar(20)   ,
	nazwisko             varchar(20)   ,
	plec                 plec NOT NULL ,
	data_urodzenia       date   ,
	miejsce_urodzenia    integer   ,
	CONSTRAINT pk_osoby_id PRIMARY KEY ( id ),
	CONSTRAINT fk_osoby_kraj FOREIGN KEY ( miejsce_urodzenia ) REFERENCES kraj( id )   
 );

CREATE  TABLE pozycja ( 
	id                   integer  NOT NULL ,
	nazwa                varchar(20)  NOT NULL ,
	CONSTRAINT pk_pozycja_id PRIMARY KEY ( id ),
	CONSTRAINT unq_pozycja_name UNIQUE ( nazwa )
 );

CREATE  TABLE produkcja ( 
	id_filmu             integer  NOT NULL ,
	id_osoba             integer   ,
	id_pozycja           integer   ,
	CONSTRAINT fk_tworcy_filmy FOREIGN KEY ( id_filmu ) REFERENCES film( id )   ,
	CONSTRAINT fk_produkcja_osoby FOREIGN KEY ( id_osoba ) REFERENCES osoby( id )   ,
	CONSTRAINT fk_produkcja_pozycja FOREIGN KEY ( id_pozycja ) REFERENCES pozycja( id )   ,
	UNIQUE(id_filmu,id_osoba,id_pozycja)
 );

CREATE  TABLE sala ( 
	id                   integer  NOT NULL ,
	liczba_rzedow        integer  NOT NULL ,
	miejsca_w_rzedzie    integer  NOT NULL ,
	CONSTRAINT pk_sala_id PRIMARY KEY ( id )
 );

CREATE  TABLE seans ( 
	id                   integer  NOT NULL ,
	id_filmu             integer  NOT NULL ,
	data_rozpoczecia     timestamp  NOT NULL ,
	id_sala              integer  NOT NULL ,
	"3d"                 boolean  NOT NULL ,
	cena                 numeric(2)  NOT NULL ,
	dzwiek               dzwiek_type   ,
	napisy               boolean  NOT NULL ,
	CONSTRAINT pk_seans_id PRIMARY KEY ( id ),
	CONSTRAINT fk_seans_film FOREIGN KEY ( id_filmu ) REFERENCES film( id )   ,
	CONSTRAINT fk_seans_sala FOREIGN KEY ( id_sala ) REFERENCES sala( id )   
 );

CREATE  TABLE wytwornia ( 
	id                   integer  NOT NULL ,
	studio               varchar(30)  NOT NULL ,
	CONSTRAINT pk_producent_id PRIMARY KEY ( id ),
	CONSTRAINT unq_studio_name UNIQUE ( studio )
 );

CREATE  TABLE zamowienie ( 
	id                   integer  NOT NULL ,
	id_seansu            integer NOT NULL,
	data_zamowienia      timestamp  NOT NULL default current_date,
	zrealizowane         boolean  NOT NULL ,
	id_klient            integer   ,
	CONSTRAINT unq_zamowienia_id UNIQUE ( id ) ,
	CONSTRAINT fk_zamowienie_klient FOREIGN KEY ( id_klient ) REFERENCES klient( id )  ,
	CONSTRAINT fk_id_seansu FOREIGN KEY ( id_seansu ) REFERENCES seans( id )
 );

CREATE  TABLE znizka ( 
	id_znizki            integer  NOT NULL ,
	rodzaj               varchar(20)  NOT NULL ,
	procent_znizki       numeric(2)  NOT NULL ,
	CONSTRAINT pk_znizka_id_znizki PRIMARY KEY ( id_znizki ),
	CONSTRAINT unq_rodzaj_name UNIQUE ( rodzaj )
 );

CREATE  TABLE bilet ( 
	id_zamowienia        integer  NOT NULL ,
	id_znizki            integer  NOT NULL ,
	numer_rzedu          integer  NOT NULL ,
	numer_miejsca        integer  NOT NULL ,
	CONSTRAINT fk_bilet_znizka FOREIGN KEY ( id_znizki ) REFERENCES znizka( id_znizki )   ,
	CONSTRAINT fk_bilet_zamowienia FOREIGN KEY ( id_zamowienia ) REFERENCES zamowienie( id )   
 );

CREATE  TABLE film_gatunek ( 
	id_filmu             integer  NOT NULL ,
	id_gatunku           integer  NOT NULL ,
	CONSTRAINT fk_film_gatunki FOREIGN KEY ( id_filmu ) REFERENCES film( id )   ,
	CONSTRAINT fk_film_gatunki_gatunek FOREIGN KEY ( id_gatunku ) REFERENCES gatunek( id )   ,
	UNIQUE(id_filmu,id_gatunku)
 );

CREATE  TABLE film_jezyk ( 
	id_filmu             integer  NOT NULL ,
	id_jezyk             integer  NOT NULL ,
	CONSTRAINT fk_film_jezyk_film_jezyk FOREIGN KEY ( id_jezyk ) REFERENCES jezyk( id )   ,
	CONSTRAINT fk_film_jezyk_film FOREIGN KEY ( id_filmu ) REFERENCES film( id )   ,
	UNIQUE(id_filmu,id_jezyk)
 );

CREATE  TABLE film_kraj ( 
	id_filmu             integer  NOT NULL ,
	id_kraj              integer  NOT NULL ,
	CONSTRAINT fk_film_kraj_kraj FOREIGN KEY ( id_kraj ) REFERENCES kraj( id )   ,
	CONSTRAINT fk_film_kraj_film FOREIGN KEY ( id_filmu ) REFERENCES film( id )   ,
	UNIQUE (id_filmu,id_kraj)
 );

CREATE  TABLE film_wytwornia ( 
	id_filmu             integer  NOT NULL ,
	id_wytwornia         integer  NOT NULL ,
	CONSTRAINT fk_film_wytwornia_wytwornia FOREIGN KEY ( id_wytwornia ) REFERENCES wytwornia( id )   ,
	CONSTRAINT fk_film_wytwornia_film FOREIGN KEY ( id_filmu ) REFERENCES film( id )   ,
	UNIQUE (id_filmu,id_wytwornia)
 );

CREATE  TABLE historia_ocen ( 
	id_filmu             integer  NOT NULL ,
	id_klienta           integer  NOT NULL ,
	ocena                integer  NOT NULL ,
	data_wystawienia     date  NOT NULL default current_date,
	komentarz            text  NOT NULL ,
	CONSTRAINT fk_historia_ocen_klient FOREIGN KEY ( id_klienta ) REFERENCES klient( id )   ,
	CONSTRAINT fk_historia_ocen_film FOREIGN KEY ( id_filmu ) REFERENCES film( id )   
 );

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

 CREATE TRIGGER klient_ins BEFORE INSERT ON klient FOR EACH ROW EXECUTE PROCEDURE klient_ins();
 CREATE TRIGGER klient_upd BEFORE UPDATE ON klient FOR EACH ROW EXECUTE PROCEDURE klient_upd();

 CREATE TRIGGER gatunek_ins BEFORE INSERT ON gatunek FOR EACH ROW EXECUTE PROCEDURE klient_ins();
 CREATE TRIGGER gatunek_upd BEFORE UPDATE ON gatunek FOR EACH ROW EXECUTE PROCEDURE klient_upd();

 CREATE TRIGGER jezyk_ins BEFORE INSERT ON jezyk FOR EACH ROW EXECUTE PROCEDURE klient_ins();
 CREATE TRIGGER jezyk_upd BEFORE UPDATE ON jezyk FOR EACH ROW EXECUTE PROCEDURE klient_upd();

 CREATE TRIGGER wytwornia_ins BEFORE INSERT ON wytwornia FOR EACH ROW EXECUTE PROCEDURE klient_ins();
 CREATE TRIGGER wytwornia_upd BEFORE UPDATE ON wytwornia FOR EACH ROW EXECUTE PROCEDURE klient_upd();

 CREATE TRIGGER kraj_ins BEFORE INSERT ON kraj FOR EACH ROW EXECUTE PROCEDURE klient_ins();
 CREATE TRIGGER kraj_upd BEFORE UPDATE ON kraj FOR EACH ROW EXECUTE PROCEDURE klient_upd();

 CREATE TRIGGER osoby_ins BEFORE INSERT ON osoby FOR EACH ROW EXECUTE PROCEDURE klient_ins();
 CREATE TRIGGER osoby_upd BEFORE UPDATE ON osoby FOR EACH ROW EXECUTE PROCEDURE klient_upd();

 CREATE TRIGGER pozycja_ins BEFORE INSERT ON pozycja FOR EACH ROW EXECUTE PROCEDURE klient_ins();
 CREATE TRIGGER pozycja_upd BEFORE UPDATE ON pozycja FOR EACH ROW EXECUTE PROCEDURE klient_upd();

 CREATE TRIGGER film_ins BEFORE INSERT ON film FOR EACH ROW EXECUTE PROCEDURE klient_ins();
 CREATE TRIGGER film_upd BEFORE UPDATE ON film FOR EACH ROW EXECUTE PROCEDURE klient_upd();

 CREATE TRIGGER seans_ins BEFORE INSERT ON seans FOR EACH ROW EXECUTE PROCEDURE klient_ins();
 CREATE TRIGGER seans_upd BEFORE UPDATE ON seans FOR EACH ROW EXECUTE PROCEDURE klient_upd();

 CREATE TRIGGER zamowienie_ins BEFORE INSERT ON zamowienie FOR EACH ROW EXECUTE PROCEDURE klient_ins();
 CREATE TRIGGER zamowienie_upd BEFORE UPDATE ON zamowienie FOR EACH ROW EXECUTE PROCEDURE klient_upd();

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
 $$ language plpgsql IMMUTABLE;

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

 CREATE OR REPLACE VIEW seans_miejsca as SELECT z.*, wolne_miejsca(z.id) as wolne_miejsca FROM seans z;

 CREATE OR REPLACE VIEW film_komentarze AS (SELECT f.id,h.ocena, h.komentarz, h.data_wystawienia, k.imie, k.nazwisko
 FROM film f JOIN historia_ocen h ON f.id = id_filmu
  JOIN klient k ON k.id = id_klienta ORDER BY data_wystawienia);

 CREATE OR REPLACE VIEW role_osoby AS (SELECT o.id,p.nazwa,f.id as id_filmu, f.tytul, f.data_premiery
 FROM produkcja JOIN osoby o ON id_osoba = o.id JOIN
 pozycja p ON p.id = id_pozycja JOIN film f ON f.id = id_filmu);

 CREATE INDEX film_tytul ON film (tytul);
 CREATE INDEX film_ocena ON film (score(id));
 CREATE INDEX historia_data ON historia_ocen (data_wystawienia);
 CREATE INDEX seans_data ON seans (data_rozpoczecia);

 CREATE OR REPLACE FUNCTION seans_data_check() RETURNS TRIGGER AS $$
 DECLARE
     czas int;
 BEGIN
     IF (SELECT data_premiery FROM film f WHERE f.id = NEW.id_filmu) > NEW.data_rozpoczecia THEN
         RETURN NULL;
     END IF;
     czas = (SELECT czas_trwania FROM film WHERE film.id = NEW.id_filmu);
     IF EXISTS (SELECT * FROM seans s JOIN film f ON s.id_filmu = f.id WHERE s.id_sala = NEW.id_sala AND s.id != NEW.id
     AND s.data_rozpoczecia <= NEW.data_rozpoczecia + ((czas+30) * interval '1 minute') AND
     s.data_rozpoczecia + ((f.czas_trwania + 30) * interval '1 minute') >= NEW.data_rozpoczecia) THEN
         RETURN NULL;
     END IF;
     RETURN NEW;
 END;
 $$ language plpgsql;

 CREATE TRIGGER seans_data_check BEFORE INSERT OR UPDATE ON seans FOR EACH ROW EXECUTE PROCEDURE seans_data_check();

 CREATE OR REPLACE FUNCTION zajete_miejsca_check() RETURNS TRIGGER AS $$
 BEGIN
     IF NEW.numer_rzedu > (SELECT liczba_rzedow FROM zamowienie z JOIN seans s ON z.id_seansu = s.id
     JOIN sala ON sala.id = s.id_sala WHERE z.id = NEW.id_zamowienia) THEN
         RETURN NULL;
     END IF;
     IF NEW.numer_miejsca > (SELECT miejsca_w_rzedzie FROM zamowienie z JOIN seans s ON z.id_seansu = s.id
     JOIN sala ON sala.id = s.id_sala WHERE z.id = NEW.id_zamowienia) THEN
         RETURN NULL;
     END IF;
     IF NEW.numer_rzedu <= 0 OR NEW.numer_miejsca <= 0 THEN
         RETURN NULL;
     END IF;
     IF EXISTS(SELECT * FROM bilet b JOIN zamowienie z ON z.id = b.id_zamowienia JOIN seans s ON s.id = z.id_seansu
     WHERE b.numer_rzedu = NEW.numer_rzedu AND b.numer_miejsca = NEW.numer_miejsca AND s.id = (SELECT s.id FROM
     seans s JOIN zamowienie z ON s.id = z.id_seansu WHERE z.id = NEW.id_zamowienia)) THEN
         RETURN NULL;
     END IF;
     RETURN NEW;
 END;
 $$ language plpgsql;

 CREATE TRIGGER zajete_miejsca_check BEFORE INSERT OR UPDATE ON bilet FOR EACH ROW EXECUTE PROCEDURE zajete_miejsca_check();