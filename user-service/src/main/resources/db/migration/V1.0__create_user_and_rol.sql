create sequence user_sequence start with 1 increment by 1;

create table user (
    id bigint not null,
    name varchar(255) not null,
    password varchar(255) not null,
    email varchar(320) not null,
    state bit not null,
    created_by varchar(255) not null,
    created_date datetime(6) not null,
    modified_by varchar(255) not null,
    last_modified_date datetime(6) not null,
    constraint PK_USER primary key (id),
    constraint UQ_USER_NAME unique (name),
    constraint UQ_USER_EMAIL unique (email)
);

create table user_roles (
    user_id bigint not null,
    role varchar(255) not null,
    constraint PK_user_roles primary key (user_id, role),
    constraint FK_user foreign key (user_id) references user (id)
);

insert into user (name, password, email, state, id, created_by, created_date, modified_by, last_modified_date)
values ('Mariano', '$2a$10$7QuN7hTOFMgU5vgewjHJZOxdtAYpN8ATgE1itj9JNFKSS3dEeklTC', 'm_villa@hotmail.com', true, user_sequence.nextval,
        'USER-SERVICE', CURRENT_TIMESTAMP, 'USER-SERVICE', CURRENT_TIMESTAMP);
insert into user_roles (user_id, role) values (user_sequence.currval, 'USER');
insert into user_roles (user_id, role) values (user_sequence.currval, 'ADMIN');