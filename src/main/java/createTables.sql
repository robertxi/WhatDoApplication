
create table task (
  id BIGINT NOT NULL AUTO_INCREMENT,

	user_id INT not null, /*LONG,FLAT*/
	title VARCHAR(30) not null,
	description VARCHAR(200) not null,
	date_created VARCHAR(30) not null,
	date_modified VARCHAR(30) not null,
	primary key(id)
);

create table task_item(
	id BIGINT NOT NULL AUTO_INCREMENT,
    task_id BIGINT NOT NULL,
    content VARCHAR(300) not null,
	date_created VARCHAR(30) not null,
	date_modified VARCHAR(30) not null,
    status VARCHAR(30) not null,
    FOREIGN KEY (task_id) REFERENCES task(id),
    primary key(id)
);

create table comments(
	id BIGINT NOT NULL AUTO_INCREMENT,
    taskitem_id BIGINT NOT NULL,
    content VARCHAR(300) not null,
    date_created varchar(30) not null,
    FOREIGN KEY (taskitem_id) REFERENCES task_item(id),
    primary key(id)
);

create table users(
    id BIGINT NOT NULL AUTO_INCREMENT,
    username VARCHAR(50),
    password VARCHAR(50),
    fName varchar(50),
    lName VARCHAR(50),
    email VARCHAR(50),
    primary key(id)
);