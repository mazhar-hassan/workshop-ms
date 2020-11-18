INSERT INTO tv_actors(id, name) VALUES
(1, 'Jon Travolta (JU)'),
(2, 'Nickolas Cage (JU)'),
(3, 'Van Damn (JU)'),
(4, 'Jason Stetham (JU)'),
(5, 'Silvester Stylune (JU)'),
(6, 'Arnald Schewaznager (JU)'),
(7, 'Harrison Ford (JU)');

INSERT INTO tv_movies(id, title, description) VALUES
(1, 'Terminator 1 (JU)', 'Terminator is all about bots to take over human race'),
(2, 'Skyfall (JU)', 'Snowfall is all about forgotten movie'),
(3, 'Rambo III (JU)', 'A tribute to gallant people of Afghanistan by Hollywood'),
(4, 'Commando (JU)', 'Arnold Shewazniger movie'),
(5, 'Rambo II (JU)', 'Silvester movie that got famous');

INSERT INTO tv_movie_generas(id, genera, movie_id) VALUES
(1, 'ACTION', 1),
(2, 'ADVENTURE', 1),
(3, 'DRAMA', 2),
(4, 'SPORTS',3),
(5, 'COMEDY',5),
(6, 'WAR',5);

INSERT INTO tv_movie_publishers(id, publisher, country, publish_date, movie_id) VALUES
(1, 'Pub1', 'US', '2020-11-12T07:50:11+21:31', 1),
(2, 'Pub1', 'UK', '2020-11-13T14:30:22+41:51', 2),
(3, 'Pub1', 'US', '2020-11-13T14:30:33+61:71', 4);
