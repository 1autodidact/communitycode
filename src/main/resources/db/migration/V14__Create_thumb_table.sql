create table thumb
(
	id bigint auto_increment,
	thumb_id bigint not null,
	thumb_id_parent bigint not null,
	constraint thumb_pk
		primary key (id)
);

