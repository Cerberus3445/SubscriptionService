package ru.gentleman.subscription.mapper;

import org.mapstruct.Mapper;
import ru.gentleman.subscription.dto.SubscriptionDto;
import ru.gentleman.subscription.entity.Subscription;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SubscriptionMapper {

    SubscriptionDto toDto(Subscription entity);

    Subscription toEntity(SubscriptionDto dto);

    List<SubscriptionDto> toDto(List<Subscription> entities);
}
