package com.q.colabtaskmanagement.mapper;

import com.q.colabtaskmanagement.common.dto.invite.InviteDTO;
import com.q.colabtaskmanagement.dataaccess.model.sql.Invite;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InviteMapper {
    InviteDTO toDTO(Invite invite);
}
