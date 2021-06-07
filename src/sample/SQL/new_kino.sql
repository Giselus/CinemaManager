CREATE  TABLE film (
	id                   integer  NOT NULL ,
	tytul                varchar(50)  NOT NULL ,
	data_premiery        date   ,
	czas_trwania         integer  NOT NULL ,
	plakat               bytea   ,
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
	data_urodzenia       date  NOT NULL ,
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
	CONSTRAINT fk_produkcja_pozycja FOREIGN KEY ( id_pozycja ) REFERENCES pozycja( id )   
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
	CONSTRAINT fk_film_gatunki_gatunek FOREIGN KEY ( id_gatunku ) REFERENCES gatunek( id )   
 );

CREATE  TABLE film_jezyk ( 
	id_filmu             integer  NOT NULL ,
	id_jezyk             integer  NOT NULL ,
	CONSTRAINT fk_film_jezyk_film_jezyk FOREIGN KEY ( id_jezyk ) REFERENCES jezyk( id )   ,
	CONSTRAINT fk_film_jezyk_film FOREIGN KEY ( id_filmu ) REFERENCES film( id )   
 );

CREATE  TABLE film_kraj ( 
	id_filmu             integer  NOT NULL ,
	id_kraj              integer  NOT NULL ,
	CONSTRAINT fk_film_kraj_kraj FOREIGN KEY ( id_kraj ) REFERENCES kraj( id )   ,
	CONSTRAINT fk_film_kraj_film FOREIGN KEY ( id_filmu ) REFERENCES film( id )   
 );

CREATE  TABLE film_wytwornia ( 
	id_filmu             integer  NOT NULL ,
	id_wytwornia         integer  NOT NULL ,
	CONSTRAINT fk_film_wytwornia_wytwornia FOREIGN KEY ( id_wytwornia ) REFERENCES wytwornia( id )   ,
	CONSTRAINT fk_film_wytwornia_film FOREIGN KEY ( id_filmu ) REFERENCES film( id )   
 );

CREATE  TABLE historia_ocen ( 
	id_filmu             integer  NOT NULL ,
	id_klienta           integer  NOT NULL ,
	ocena                integer  NOT NULL ,
	data_wystawienia     date  NOT NULL default current_date,
	komentarz            varchar(250)  NOT NULL ,
	CONSTRAINT fk_historia_ocen_klient FOREIGN KEY ( id_klienta ) REFERENCES klient( id )   ,
	CONSTRAINT fk_historia_ocen_film FOREIGN KEY ( id_filmu ) REFERENCES film( id )   
 );