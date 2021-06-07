INSERT INTO jezyk VALUES
	(1, 'english'),
	(2, 'french'),
	(3, 'german');

INSERT INTO wytwornia VALUES (1, 'Columbia Pictures');
INSERT INTO wytwornia VALUES (2, '20th Century Fox');
INSERT INTO wytwornia VALUES (3, 'Warner Bros. Pictures');
INSERT INTO wytwornia VALUES (4, 'Universal Pictures');

INSERT INTO gatunek VALUES
    (1, 'Action'),
    (2, 'Thriller'),
    (3, 'SF'),
    (4, 'Drama'),
    (5, 'Biography'),
    (6, 'Criminal'),
    (7, 'Comedy'),
    (8, 'Animation');
    
INSERT INTO kraj VALUES(1, 'United States');
INSERT INTO kraj VALUES(2, 'Canada');
INSERT INTO kraj VALUES(3, 'France');
INSERT INTO kraj VALUES(4, 'Great Britain');
INSERT INTO kraj VALUES(5, 'German');
INSERT INTO kraj VALUES(6, 'New Zeland');

INSERT INTO pozycja VALUES(1, 'Director');
INSERT INTO pozycja VALUES(2, 'Actor');
INSERT INTO pozycja VALUES(3, 'Composer');
INSERT INTO pozycja VALUES(4, 'Writer');

INSERT INTO osoby VALUES (1, 'Quentin', 'Tarantino', 'mezczyzna', to_date('27/03/1963', 'DD/MM/YYYY'), 1);
INSERT INTO osoby VALUES (2, 'Daniel', 'Radcliffe', 'mezczyzna', to_date('23/07/1989', 'DD/MM/YYYY'), 4);
INSERT INTO osoby VALUES (3, 'Emma', 'Watson', 'kobieta', to_date('15/04/1990', 'DD/MM/YYYY'), 4);
INSERT INTO osoby VALUES (4, 'Brad', 'Pitt', 'mezczyzna', to_date('18/12/1963', 'DD/MM/YYYY'), 1);
INSERT INTO osoby VALUES (5, 'Edgar', 'Wright', 'mezczyzna', to_date('18/04/1974', 'DD/MM/YYYY'), 4);
INSERT INTO osoby VALUES (6, 'James', 'Cameron', 'mezczyzna', to_date('16/08/1954', 'DD/MM/YYYY'), 2);
INSERT INTO osoby VALUES (7, 'Hans', 'Zimmer', 'mezczyzna', to_date('12/09/1957', 'DD/MM/YYYY'), 5);
INSERT INTO osoby VALUES (8, 'Melanie', 'Laurent', 'kobieta', to_date('21/02/1983', 'DD/MM/YYYY'), 3);
INSERT INTO osoby VALUES (9, 'Morgan', 'Freeman', 'mezczyzna', to_date('01/06/1937', 'DD/MM/YYYY'), 1);
INSERT INTO osoby VALUES (10, 'Michael', 'Cera', 'mezczyzna', to_date('07/06/1988', 'DD/MM/YYYY'), 4);

INSERT INTO film VALUES(1, 'Django Unchained', to_date('25/12/2012', 'DD/MM/YYYY'), 165);
INSERT INTO film VALUES(2, 'Avatar', to_date('25/12/2009', 'DD/MM/YYYY'), 162);
INSERT INTO film VALUES(3, '8 mile', to_date('08/11/2002', 'DD/MM/YYYY'), 110);
INSERT INTO film VALUES(4, 'Inglourious Basterds', to_date('20/05/2009', 'DD/MM/YYYY'), 153);
INSERT INTO film VALUES(5, 'Reservoir Dogs', to_date('21/01/1992', 'DD/MM/YYYY'), 99);
INSERT INTO film VALUES(6, 'Pulp fiction', to_date('14/10/1994', 'DD/MM/YYYY'), 154);
INSERT INTO film VALUES(7, 'Four Rooms', to_date('25/12/1995', 'DD/MM/YYYY'), 98);
INSERT INTO film VALUES(8, 'Kill Bill: Volume 1', to_date('10/10/2003', 'DD/MM/YYYY'), 111);
INSERT INTO film VALUES(9, 'Kill Bill: Volume 2', to_date('16/04/2004', 'DD/MM/YYYY'), 136);
INSERT INTO film VALUES(10, 'Sin City', to_date('01/04/2005', 'DD/MM/YYYY'), 124);
INSERT INTO film VALUES(11, 'American Psycho', to_date('14/04/2000', 'DD/MM/YYYY'), 101);
INSERT INTO film VALUES(12, 'Hot Fuzz', to_date('16/02/2007', 'DD/MM/YYYY'), 121);
INSERT INTO film VALUES(13, 'Scott Pilgrim vs. the World', to_date('27/07/2010', 'DD/MM/YYYY'), 112);
INSERT INTO film VALUES(14, 'Baby Driver', to_date('11/03/2017', 'DD/MM/YYYY'), 113);
INSERT INTO film VALUES(15, 'Harry Potter and the Philosophers Stone', to_date('04/11/2001', 'DD/MM/YYYY'), 152);
INSERT INTO film VALUES(16, 'Harry Potter and the Chamber of Secrets', to_date('03/11/2002', 'DD/MM/YYYY'), 161);
INSERT INTO film VALUES(17, 'Harry Potter and the Order of the Phoenix', to_date('28/06/2007', 'DD/MM/YYYY'), 138);
INSERT INTO film VALUES(18, 'Harry Potter and the Half-Blood Prince', to_date('07/07/2009', 'DD/MM/YYYY'), 153);
INSERT INTO film VALUES(19, 'Harry Potter and the Prisoner of Azkaban', to_date('23/05/2004', 'DD/MM/YYYY'), 142);
INSERT INTO film VALUES(20, 'Seven', to_date('15/09/1995', 'DD/MM/YYYY'), 127);


INSERT INTO produkcja VALUES(1, 1, 1);
INSERT INTO produkcja VALUES(7, 1, 1);
INSERT INTO produkcja VALUES(7, 1, 2);
INSERT INTO produkcja VALUES(1, 1, 4);
INSERT INTO produkcja VALUES(15, 3, 2);
INSERT INTO produkcja VALUES(15, 2, 2);
INSERT INTO produkcja VALUES(20, 9, 2);
INSERT INTO produkcja VALUES(12, 10, 2);
INSERT INTO produkcja VALUES(12, 5, 1);
INSERT INTO produkcja VALUES(2, 7, 3);
INSERT INTO produkcja VALUES(10, 1, 4);
INSERT INTO produkcja VALUES(4, 8, 2);
INSERT INTO produkcja VALUES(2, 6, 1);

INSERT INTO film_kraj VALUES(1, 1);
INSERT INTO film_kraj VALUES(2, 1);
INSERT INTO film_kraj VALUES(2, 6);
INSERT INTO film_kraj VALUES(3, 1);
INSERT INTO film_kraj VALUES(4, 1);
INSERT INTO film_kraj VALUES(4, 3);
INSERT INTO film_kraj VALUES(4, 5);
INSERT INTO film_kraj VALUES(5, 1);
INSERT INTO film_kraj VALUES(6, 1);
INSERT INTO film_kraj VALUES(7, 1);
INSERT INTO film_kraj VALUES(8, 1);
INSERT INTO film_kraj VALUES(9, 1);
INSERT INTO film_kraj VALUES(10, 1);
INSERT INTO film_kraj VALUES(11, 1);
INSERT INTO film_kraj VALUES(12, 4);
INSERT INTO film_kraj VALUES(13, 4);
INSERT INTO film_kraj VALUES(14, 4);
INSERT INTO film_kraj VALUES(15, 4);
INSERT INTO film_kraj VALUES(16, 4);
INSERT INTO film_kraj VALUES(17, 4);
INSERT INTO film_kraj VALUES(18, 4);
INSERT INTO film_kraj VALUES(19, 4);
INSERT INTO film_kraj VALUES(20, 1);

INSERT INTO film_gatunek VALUES
	(1, 1),
	(2, 3),
	(2, 1),
	(20, 6),
	(20, 2),
	(12, 6),
	(12, 7),
	(14, 1),
	(14, 7);
	
INSERT INTO film_jezyk VALUES
	(1, 1),
	(2, 1),
	(3, 1),
	(4, 2),
	(5, 1),
	(6, 1),
	(7, 1),
	(8, 1),
	(9, 1),
	(10, 1),
	(11, 1),
	(12, 1),
	(13, 1),
	(14, 1),
	(15, 1),
	(16, 1),
	(17, 1),
	(18, 1),
	(19, 1),
	(20, 1),
	(4, 2),
	(4, 3);
	
INSERT INTO film_wytwornia VALUES
	(1, 1),
	(2, 2),
	(3, 2),
	(4, 1),
	(5, 4),
	(6, 2),
	(7, 1),
	(8, 2),
	(9, 2),
	(10, 1),
	(11, 4),
	(12, 2),
	(13, 1),
	(14, 2),
	(15, 2),
	(16, 1),
	(17, 4),
	(18, 2),
	(19, 4),
	(20, 3);

INSERT INTO sala VALUES (1, 8, 15), (2, 6, 12);

INSERT INTO seans VALUES
    (1, 1,TIMESTAMP '2021-06-15 16:00:00', 1, false, 15, 'oryginal', true),
    (2, 2,TIMESTAMP '2021-06-14 16:00:00', 2, false, 15, 'oryginal', true),
    (3, 4,TIMESTAMP '2021-06-14 16:00:00', 1, false, 15, 'oryginal', true),
    (4, 3,TIMESTAMP '2021-06-15 16:00:00', 2, false, 15, 'oryginal', true),
    (5, 5,TIMESTAMP '2021-06-16 16:00:00', 1, false, 15, 'oryginal', true);


INSERT INTO znizka VALUES
	(1, 'uczniowska', 20),
	(2, 'emerycka', 25),
	(3, 'weterańska', 50),
	(4, 'normalna', 0),
	(5, 'pracownicza', 90);

INSERT INTO zamowienie VALUES
	(1, 1, TIMESTAMP '2021-06-06 15:00:00', true),
	(2, 1, TIMESTAMP '2021-06-06 14:00:00', true),
	(3, 1, TIMESTAMP '2021-06-06 13:00:00', true);

INSERT INTO bilet VALUES
	(1, 4, 1, 1),
	(1, 4, 1, 2),
	(1, 4, 1, 3),
	(1, 4, 1, 4),
	(2, 4, 2, 2),
	(2, 4, 2, 3),
	(2, 4, 2, 4),
	(2, 4, 2, 5),
	(3, 4, 3, 7),
	(3, 4, 3, 8);