create table if not exists country
(
    id        bigint  not null
        primary key,
    name      varchar(100)
        constraint uk_llidyp77h6xkeokpbmoy710d4
            unique,
    region    varchar(255),
    subregion varchar(255),
    version   integer not null
);

create table if not exists hard_skills
(
    id      bigint  not null
        primary key,
    name    varchar(255)
        constraint uk_5xsmpaaa85e90urfgt9j70t7
            unique,
    version integer not null
);

create table if not exists users
(
    id               uuid        not null
        primary key,
    version          integer     not null,
    avatar           varchar(500),
    birthdate        timestamp,
    deleted          boolean     not null,
    email            varchar(100)
        constraint uk_6dotkott2kjsp8vw4d0m25fb7
            unique,
    last_name        varchar(50),
    name           varchar(50) not null,
    nickname         varchar(50)
        constraint uk_2ty1xmrrgtn89xt7kyxx6ta7h
            unique,
    phone_number     varchar(30),
    sex              varchar(50) not null,
    surname          varchar(50) not null,
    user_description varchar(1000),
    country_id       bigint
        constraint fk5t4yyo3f0ctxayh7voqv51fmg
            references country
);

create table if not exists user_followers
(
    user_id     uuid not null
        constraint fkox7c2m7d9qhhpu45d83luq19q
            references users,
    follower_id uuid not null
        constraint fksauvjgnbgys3gbeharkga2omh
            references users
);

create table if not exists user_hard_skills
(
    user_id       uuid   not null
        constraint fk8l7tge8b0qmuf18h6bwfnefeg
            references users,
    hard_skill_id bigint not null
        constraint fk1bhiogqtbl42flxfued854n3y
            references hard_skills
);

