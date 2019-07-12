package com.pb.service1;

import au.com.dius.pact.provider.junit.Provider;
import au.com.dius.pact.provider.junit.State;
import au.com.dius.pact.provider.junit.loader.PactBroker;
import au.com.dius.pact.provider.junit.loader.PactBrokerAuth;
import au.com.dius.pact.provider.junit5.HttpTestTarget;
import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider;
import com.pb.service1.model.Employee;
import com.pb.service1.repositories.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = "server.port=8080")
@Provider("service1")
//@PactFolder("target/pacts")
@PactBroker(authentication = @PactBrokerAuth(username = "iris", password = "iris"), host = "localhost", port = "80", protocol = "http")
public class Service1ApplicationTests {

    @MockBean
    private EmployeeRepository employeeRepository;

    @BeforeEach
    void setupTestTarget(PactVerificationContext context) {
        context.setTarget(new HttpTestTarget("localhost",8080, "/"));
    }

    @TestTemplate
    @ExtendWith(PactVerificationInvocationContextProvider.class)
    void pactVerificationTestTemplate(PactVerificationContext context) {
        context.verifyInteraction();
    }

    @State({"employee exists with id 1"})
    public void toCreateOneEmployeeState() {
        Employee employee = new Employee();
        employee.setId(1L);
        employee.setName("Atmaram");
        employee.setRole("QA");
        when(employeeRepository.findById(eq(1L))).thenReturn(Optional.of(employee));
    }

    @State({"multiple employees exists"})
    public void toCreateMultipleEmployeeState() {
        List<Employee> employees = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Employee employee = new Employee();
            employee.setId(new Long(i));
            employee.setName("Atmaram " + i);
            employee.setRole("QA " + i);
            employees.add(employee);
        }

        when(employeeRepository.findAll()).thenReturn(employees);
    }

}
