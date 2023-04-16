package dev.aknb.ordersystem.repositories.order;

import dev.aknb.ordersystem.entities.Order;
import dev.aknb.ordersystem.dtos.order.OrderFilterDto;
import dev.aknb.ordersystem.entities.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CustomOrderRepositoryImpl implements CustomOrderRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<Order> findAllByFilter(OrderFilterDto orderFilterDto) {

        CriteriaBuilder cBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> cQuery = cBuilder.createQuery(Order.class);
        Root<Order> orderRoot = cQuery.from(Order.class);
        Join<Order, User> userJoin = orderRoot.join("user");

        cQuery.where(
                getPredicate(orderFilterDto, cBuilder, orderRoot, userJoin)
        );

        cQuery.orderBy(cBuilder.desc(orderRoot.get("lastModifiedDate")));

        TypedQuery<Order> orderQuery = entityManager.createQuery(cQuery);
        orderQuery.setFirstResult(orderFilterDto.getPageable().getPageNumber() * orderFilterDto.getPageable().getPageSize());
        orderQuery.setMaxResults(orderFilterDto.getPageable().getPageSize());

        List<Order> orderList = orderQuery.getResultList();

        CriteriaQuery<Long> countCQuery = cBuilder.createQuery(Long.class);
        Root<Order> countOrderRoot = countCQuery.from(Order.class);
        Join<Order, User> countUserJoin = countOrderRoot.join("user");

        countCQuery.select(cBuilder.count(countOrderRoot));

        countCQuery.where(
                getPredicate(orderFilterDto, cBuilder, countOrderRoot, countUserJoin)
        );

        Long count = entityManager.createQuery(countCQuery).getSingleResult();

        return new PageImpl<>(orderList, orderFilterDto.getPageable(), count);
    }

    /**
     * Other type to getPredicate
     * Predicate orderNamePredicate = cBuilder.like(cBuilder.upper(orderRoot.get("name")), "%" + orderFilter.getSearchText().toUpperCase() + "%");
     * return cBuilder.and(orderNamePredicate);
     */
    private static Predicate getPredicate(OrderFilterDto orderFilterDto, CriteriaBuilder cBuilder, Root<Order> orderRoot, Join<Order, User> userJoin) {

        return cBuilder.and(
                cBuilder.equal(orderRoot.get("status"), orderFilterDto.getStatus()),
                cBuilder.or(
                        cBuilder.like(cBuilder.upper(orderRoot.get("name")), "%" + orderFilterDto.getSearchText().toUpperCase() + "%"),
                        cBuilder.like(cBuilder.upper(userJoin.get("fullName")), "%" + orderFilterDto.getSearchText().toUpperCase() + "%")
                )
        );
    }
}
