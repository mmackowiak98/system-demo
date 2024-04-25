package org.dpd.orderlog.mapper;

import org.dpd.orderlog.model.OrderLog;
import org.dpd.orderlog.model.OrderLogRequest;
import org.dpd.orderlog.model.OrderLogResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class OrderLogMapper {
    public abstract OrderLog toOrderLog(OrderLogRequest orderLogRequest);

    public abstract OrderLogResponse toOrderLogResponse(OrderLog order);
}
