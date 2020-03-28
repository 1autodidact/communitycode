create table thumb
(
	id BIGINT auto_increment,
	thumb_id BIGINT not null,
	thumb_id_parent BIGINT not null,
	constraint thumb_pk
		primary key (id)
);

