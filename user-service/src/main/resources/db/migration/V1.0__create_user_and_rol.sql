CREATE sequence user_sequence start WITH 1 increment BY 1;

CREATE TABLE user (
    id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(320) NOT NULL,
    state BIT NOT NULL,
    created_by VARCHAR(255) NOT NULL,
    created_date DATETIME(6) NOT NULL,
    modified_by VARCHAR(255) NOT NULL,
    last_modified_date DATETIME(6) NOT NULL,
    CONSTRAINT PK_USER PRIMARY KEY (id),
    CONSTRAINT UQ_USER_NAME UNIQUE (name),
    CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
);

CREATE TABLE user_confirmation (
    user_id BIGINT NOT NULL,
    username VARCHAR(255) NOT NULL,
    email VARCHAR (320) NOT NULL,
    status VARCHAR(255) NOT NULL,
    date DATETIME(6) NOT NULL,
    CONSTRAINT PK_USER_CONFIRMATION PRIMARY KEY (user_id, status),
    CONSTRAINT FK_CONFIRMATION_USER FOREIGN KEY (user_id) REFERENCES user (id)
);

CREATE TABLE user_roles (
    user_id BIGINT NOT NULL,
    role VARCHAR(255) NOT NULL,
    CONSTRAINT PK_user_roles PRIMARY KEY (user_id, role),
    CONSTRAINT FK_user FOREIGN KEY (user_id) REFERENCES user (id)
);

INSERT INTO user (name, password, email, state, id, created_by, created_date, modified_by, last_modified_date)
VALUES ('Mariano', '$2a$10$7QuN7hTOFMgU5vgewjHJZOxdtAYpN8ATgE1itj9JNFKSS3dEeklTC', 'm_villa@hotmail.com', true, user_sequence.nextval,
        'USER-SERVICE', CURRENT_TIMESTAMP, 'USER-SERVICE', CURRENT_TIMESTAMP);

INSERT INTO user_roles (user_id, role) VALUES (user_sequence.currval, 'USER');
INSERT INTO user_roles (user_id, role) VALUES (user_sequence.currval, 'ADMIN');

INSERT INTO user_confirmation (user_id, username, email, status, date)
VALUES (user_sequence.currval, 'Mariano', 'm_villa@hotmail.com','APPROVED', CURRENT_TIMESTAMP );