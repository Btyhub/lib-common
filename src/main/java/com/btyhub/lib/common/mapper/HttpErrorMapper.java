package com.btyhub.lib.common.mapper;

import com.btyhub.lib.common.configuration.CommonProperties.ErrorsProperties.ErrorMessage;
import com.btyhub.lib.common.model.HttpError;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface HttpErrorMapper {
    HttpError map(ErrorMessage errorMessage);
}
