package dev.aknb.ordersystem.repositories.customer;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Repository;

import dev.aknb.ordersystem.entities.Customer;
import dev.aknb.ordersystem.models.Filter;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;

@Repository
public class CustomCustomerRepositoryImpl implements CustomCustomerRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<Customer> findAllByFilter(Filter filter) {

        CriteriaBuilder cBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Customer> cQuery = cBuilder.createQuery(Customer.class);
        Root<Customer> customerRoot = cQuery.from(Customer.class);

        cQuery.where(
                getPredicate(filter, cBuilder, customerRoot));

        cQuery.orderBy(
                cBuilder.desc(customerRoot.get("lastModifiedDate")));

        TypedQuery<Customer> cTypedQuery = entityManager.createQuery(cQuery);
        cTypedQuery.setFirstResult(filter.getPageable().getPageNumber() * filter.getPageable().getPageSize());
        cTypedQuery.setMaxResults(filter.getPageable().getPageSize());

        List<Customer> customers = cTypedQuery.getResultList();

        CriteriaQuery<Long> countCQuery = cBuilder.createQuery(Long.class);
        Root<Customer> countCRoot = countCQuery.from(Customer.class);

        countCQuery.select(cBuilder.count(countCRoot));

        countCQuery.where(
                getPredicate(filter, cBuilder, countCRoot));

        Long count = entityManager.createQuery(countCQuery).getSingleResult();

        return new PageImpl<>(customers, filter.getPageable(), count);
    }

    private static Predicate getPredicate(Filter filter, CriteriaBuilder cBuilder, Root<Customer> customerRoot) {

        return cBuilder.like(cBuilder.upper(customerRoot.get("fullName")),
                "%" + filter.getSearchText().toUpperCase() + "%");
    }
}
