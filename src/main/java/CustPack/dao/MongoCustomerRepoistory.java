package CustPack.dao;

import CustPack.entity.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Using spring mongo data abilities for presenting the basic api of the repository
 */
public interface MongoCustomerRepoistory extends MongoRepository<Customer, String> {

}
