Drop Table if EXISTS t_user;
CREATE TABLE t_user  (
     `id` varchar(32)    NOT NULL COMMENT 'The id of user, use UUID to generate',
     `username` varchar(255)   NULL DEFAULT NULL COMMENT 'The name of user',
     `email` varchar(255)    NOT NULL COMMENT 'The email of user',
     `password` varchar(32)   NOT NULL COMMENT 'The password of user, use md5 to encryption',
     `create_time` datetime NULL DEFAULT NULL COMMENT 'The creat_time of account',
     `avatar_path` varchar(255)    NULL DEFAULT NULL COMMENT 'The url path refer to the avatar of user',
     `last_login_time` datetime NULL DEFAULT NULL COMMENT 'Last time the user login',
     PRIMARY KEY (`id`)
);
DROP TABLE IF EXISTS t_user_vocabulary;
CREATE TABLE t_user_vocabulary (
 `user_id` varchar(32) NOT NULL COMMENT 'The user id',
 `word` varchar(255) NOT NULL COMMENT 'The word that this user add in their repository',
 `add_time` datetime NULL DEFAULT NULL COMMENT 'The time adding the word',
 `last_review_time` datetime NULL DEFAULT NULL COMMENT 'The time that this word been reviewed',
 `review_times` int NULL DEFAULT NULL COMMENT 'The times that this word been reviewed',
 `ef` double NULL DEFAULT 2.5,
 `next_review_time` datetime NULL DEFAULT NULL COMMENT 'The time that this word been reviewed',
 `used` int NULL DEFAUlT NULL,
 `translation` text NULL DEFAULT NULL,
 `definition` text NULL DEFAULT NULL,
 `pronunciation` text NULL DEFAULT NULL,
 PRIMARY KEY (`user_id`, `word`)
);

DROP TABLE IF EXISTS t_paragraph;
CREATE TABLE t_paragraph  (
     `id` varchar(32) NOT NULL COMMENT 'The id of paragraph, UUID',
     `user_id` varchar(32) NOT NULL,
     `title` varchar(255) NULL DEFAULT NULL COMMENT 'The title of paragraph',
     `content` text NULL COMMENT 'The content of paragraph',
     `create_time` datetime NULL DEFAULT NULL COMMENT 'The creat time of paragraph',
     `read_status` int,
     `marked_content` text NULL COMMENT 'The content of paragraph',
     `translation_content` text NULL COMMENT  'The translation of this paragraph',
     PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS t_paragraph_vocabulary;
CREATE TABLE t_paragraph_vocabulary(
     `paragraph_id` varchar(32) NOT NULL COMMENT 'The id of paragraph',
     `word` varchar(255) NOT NULL COMMENT 'The word. Shows the relation of paragraph and word',
     PRIMARY KEY (`paragraph_id`, `word`)
);

DROP TABLE IF EXISTS t_vocabulary;
CREATE TABLE t_vocabulary (
     `word` varchar(255) NOT NULL,
     `phonetic` varchar(255) NULL DEFAULT NULL,
     `definition` text NULL DEFAULT NULL,
     `translation` text NULL DEFAULT NULL,
     `pos` varchar(255) NULL DEFAULT NULL,
     `collins` varchar(255) NULL DEFAULT NULL,
     `oxford` varchar(255) NULL DEFAULT NULL,
     `tag` varchar(255) NULL DEFAULT NULL,
     `bnc` varchar(255) NULL DEFAULT NULL,
     `frq` varchar(255) NULL DEFAULT NULL,
     `exchange` varchar(255) NULL DEFAULT NULL,
     `detail` varchar(255) NULL DEFAULT NULL,
     `audio` varchar(255) NULL DEFAULT NULL,
      PRIMARY KEY (`word`)
);



