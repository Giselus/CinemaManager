DROP TYPE IF EXISTS dzwiek_type CASCADE;
DROP TYPE IF EXISTS plec CASCADE;
DROP TABLE IF EXISTS film CASCADE;
DROP TABLE IF EXISTS film_jezyk CASCADE;
DROP TABLE IF EXISTS film_wytwornia CASCADE;
DROP TABLE IF EXISTS film_kraj CASCADE;
DROP TABLE IF EXISTS film_gatunek CASCADE;
DROP TABLE IF EXISTS gatunek CASCADE;
DROP TABLE IF EXISTS jezyk CASCADE;
DROP TABLE IF EXISTS wytwornia CASCADE;
DROP TABLE IF EXISTS klient CASCADE;
DROP TABLE IF EXISTS kraj CASCADE;
DROP TABLE IF EXISTS produkcja CASCADE;
DROP TABLE IF EXISTS pozycja CASCADE;
DROP TABLE IF EXISTS osoby CASCADE;
DROP TABLE IF EXISTS pozycja CASCADE;
DROP TABLE IF EXISTS sala CASCADE;
DROP TABLE IF EXISTS seans CASCADE;
DROP TABLE IF EXISTS zamowienie CASCADE;
DROP TABLE IF EXISTS znizka CASCADE;
DROP TABLE IF EXISTS bilet CASCADE;
DROP TABLE IF EXISTS historia_ocen CASCADE;

DROP SEQUENCE IF EXISTS idSeq CASCADE;
DROP VIEW IF EXISTS film_filtry;
DROP VIEW IF EXISTS bilet_cena;
DROP VIEW IF EXISTS zamowienie_cena;
DROP VIEW IF EXISTS seans_miejsca;
DROP VIEW IF EXISTS film_komentarze;
DROP VIEW IF EXISTS role_osoby;