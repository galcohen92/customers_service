package CustPack.dao;

/**
 * Interface for the customer data access
 */



import CustPack.entity.Customer;

import java.util.Collection;

public interface CustomerDao {
    Customer getCustomerById(String custId);

    void deleteCustomerById(String custId);

    // Create customer and set the id that was genrate for him
    void createCustomer(Customer cust);

    void updateCustomer(Customer cust);

    Collection<Customer> getAllCustomers();
}
