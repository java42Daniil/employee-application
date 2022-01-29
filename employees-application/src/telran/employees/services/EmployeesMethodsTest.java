package telran.employees.services;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import telran.employees.dto.Employee;
import telran.employees.dto.ReturnCode;
class EmployeesMethodsTest {
private static final long ID1 = 123;
private static final String NAME = "name";
private static final LocalDate BIRTHDATE1 = LocalDate.of(2000, 1, 1);
private static final int SALARY1 = 5000;
private static final String DEPARTMENT1 = "department1";
private static final long ID2 = 124;
private static final long ID3 = 125;
private static final long ID4 = 126;
private static final long ID5 = 127;
private static final long ID6 = 128;
private static final LocalDate BIRTHDATE2 = LocalDate.of(1995, 1, 1);;
private static final LocalDate BIRTHDATE3 = LocalDate.of(1997, 1, 1);;
private static final LocalDate BIRTHDATE4 = LocalDate.of(1970, 1, 1);;
private static final LocalDate BIRTHDATE5 = LocalDate.of(1971, 1, 1);;
private static final LocalDate BIRTHDATE6 = LocalDate.of(1980, 1, 1);
private static final String DEPARTMENT2 = "department2";
private static final int SALARY2 = 6000;
private static final int SALARY3 = 1000;;
EmployeesMethods employees; 
Employee empl1 = new Employee(ID1, NAME, BIRTHDATE1, SALARY1, DEPARTMENT1);
Employee empl2 = new Employee(ID2, NAME, BIRTHDATE2, SALARY1, DEPARTMENT1);
Employee empl3 = new Employee(ID3, NAME, BIRTHDATE3, SALARY2, DEPARTMENT1);
Employee empl4 = new Employee(ID4, NAME, BIRTHDATE4, SALARY2, DEPARTMENT2);
Employee empl5 = new Employee(ID5, NAME, BIRTHDATE5, SALARY3, DEPARTMENT2);
Employee empl6 = new Employee(ID6, NAME, BIRTHDATE6, SALARY3, DEPARTMENT2);
List<Employee> employeesList = Arrays.asList(empl1,empl2,empl3,empl4,empl5,empl6);
	@BeforeEach
	void setUp() throws Exception {
		employees = new EmployeesMethodsMapsImpl();
		employeesList.forEach(employees::addEmployee);
		
	}

	@Test
	void testAddEmployee() {
		assertEquals(ReturnCode.EMPLOYEE_ALREADY_EXISTS, employees.addEmployee(empl1));
		assertEquals(ReturnCode.OK, 
				employees.addEmployee(new Employee(ID1 + 10000, NAME, BIRTHDATE1, SALARY1, DEPARTMENT1)));
	}

	@Test
	void testRemoveEmployee() {
		assertEquals(ReturnCode.OK, employees.removeEmployee(ID3));
		assertEquals(ReturnCode.EMPLOYEE_NOT_FOUND, employees.removeEmployee(ID3));	
		}

	@Test
	void testGetAllEmployees() {
		Iterable<Employee> col = employees.getAllEmployees();
		for (Employee e : col) {
			assertTrue(employeesList.contains(e));
		}
		EmployeesMethods employeesTmp = new EmployeesMethodsMapsImpl();
		List<Employee> act = (List<Employee>) (employeesTmp.getAllEmployees());
		assertTrue(act.isEmpty());	
		}

	@Test
	void testGetEmployee() {
		assertEquals(empl1, employees.getEmployee(ID1));
		assertNull(employees.getEmployee(ID1+1000));	
		}

	@Test
	void testGetEmployeesByAge() {
		assertEquals(0, ((List<Employee>) employees.getEmployeesByAge(1, 5)).size());
		assertTrue(((List<Employee>) employees.getEmployeesByAge(20, 40)).size()>0);
		}

	@Test
	void testGetEmployeesBySalary() {
		assertEquals(0, ((List<Employee>) employees.getEmployeesBySalary(2000, 4500)).size());	
		assertTrue(((List<Employee>) employees.getEmployeesBySalary(5000, 8000)).size()>0);
}

	@Test
	void testGetEmployeesByDepartment() {
		List<Employee> expected = Arrays.asList(empl1, empl2, empl3);
		assertIterableEquals(expected, employees.getEmployeesByDepartment(DEPARTMENT1));
	}

	@Test
	void testGetEmployeesByDepartmentAndSalary() {
		List<Employee> expected = Arrays.asList(empl5, empl6);
		assertIterableEquals(expected, employees.getEmployeesByDepartmentAndSalary(DEPARTMENT2, 200, 2000));	}

	@Test
	void testUpdateSalary() {
		String newDepartment = "another department";
		assertEquals(ReturnCode.OK, employees.updateDepartment(ID6, newDepartment));
		assertEquals(ReturnCode.EMPLOYEE_NOT_FOUND, employees.updateDepartment(888, newDepartment));
		assertEquals(newDepartment, employees.getEmployee(ID6).department);
		assertIterableEquals(Arrays.asList(employees.getEmployee(ID6)),
				employees.getEmployeesByDepartment(newDepartment));	}

	@Test
	void testUpdateDepartment() {
		assertEquals(ReturnCode.OK, employees.updateDepartment(ID6, "department updated"));
		assertEquals(ReturnCode.EMPLOYEE_NOT_FOUND, employees.updateDepartment(888, "department updated"));
		assertEquals("department updated", employees.getEmployee(ID6).department);
		assertIterableEquals(Arrays.asList(employees.getEmployee(ID6)),
				employees.getEmployeesByDepartment("department updated"));	}

}