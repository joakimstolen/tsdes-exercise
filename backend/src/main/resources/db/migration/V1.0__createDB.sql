create sequence hibernate_sequence start with 1 increment by 1;

create table purchase
(
    id                  bigint       not null,
    date_of_booking     date         not null,
    booked_by_userid    varchar(255) not null,
    trip_information_id bigint       not null,
    primary key (id)
);

create table trip
(
    id             bigint       not null,
    cost           bigint       not null,
    departure_date date         not null,
    description    varchar(255),
    location_name  varchar(300) not null,
    return_date    date         not null,
    title          varchar(300),
    primary key (id)
);

create table user
(
    userid          varchar(255) not null,
    email           varchar(255),
    enabled         boolean      not null,
    hashed_password varchar(255),
    last_name       varchar(128),
    name            varchar(128),
    primary key (userid)
);

create table user_booked_trips
(
    all_travelers_userid varchar(255) not null,
    booked_trips_id      bigint       not null
);

create table user_roles
(
    user_userid varchar(255) not null,
    roles       varchar(255)
);

alter table user
    add constraint UK_ob8kqyqqgmefl0aco34akdtpe unique (email);

alter table purchase
    add constraint FK5g5i7emj2oys98pvenioxt9vt foreign key (booked_by_userid) references user;

alter table purchase
    add constraint FK3tv91lsph9bbpk4lpdu231yn0 foreign key (trip_information_id) references trip;

alter table user_booked_trips
    add constraint FKfns7qb17qidjyr7fyxfpg1lym foreign key (booked_trips_id) references trip;

alter table user_booked_trips
    add constraint FKn5lstcrpf3fxue2ut10j40vft foreign key (all_travelers_userid) references user;

alter table user_roles
    add constraint FK6n7ipu0muw6e5rth6oj7a0aq0 foreign key (user_userid) references user;
