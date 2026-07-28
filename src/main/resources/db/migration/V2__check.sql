CREATE TABLE revchanges
(
    rev        BIGINT NOT NULL,
    entityname VARCHAR(255) NULL
);

CREATE TABLE revinfo
(
    rev      BIGINT NOT NULL,
    revtstmp BIGINT NULL,
    CONSTRAINT pk_revinfo PRIMARY KEY (rev)
);

CREATE TABLE tasks
(
    id       BIGINT AUTO_INCREMENT NOT NULL,
    title    VARCHAR(255) NULL,
    status   VARCHAR(255) NULL,
    priority VARCHAR(255) NULL,
    user_id  BIGINT NULL,
    CONSTRAINT pk_tasks PRIMARY KEY (id)
);

CREATE TABLE users
(
    id       BIGINT AUTO_INCREMENT NOT NULL,
    name     VARCHAR(255) NULL,
    email    VARCHAR(255) NOT NULL,
    password VARCHAR(255) NULL,
    `role`   VARCHAR(255) NULL,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

ALTER TABLE users
    ADD CONSTRAINT uc_users_email UNIQUE (email);

ALTER TABLE tasks
    ADD CONSTRAINT FK_TASKS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE revchanges
    ADD CONSTRAINT fk_revchanges_on_default_tracking_modified_entities_changelog FOREIGN KEY (rev) REFERENCES revinfo (rev);