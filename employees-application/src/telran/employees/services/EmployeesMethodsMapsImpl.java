package telran.employees.services;

import telran.employees.dto.Employee;
import telran.employees.dto.ReturnCode;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
public class EmployeesMethodsMapsImpl implements EmployeesMethods {
 private HashMap<Long, Employee> mapEmployees = new HashMap<>(); //key employee's id, value - employee
 private TreeMap<Integer, List<Employee>> employeesAge= new TreeMap<>(); //key - age, value - list of employees with the same age
 private TreeMap<Integer, List<Employee>> employeesSalary = new TreeMap<>(); //key - salary,
 //value - list of employees with the same salary
 private HashMap<String, List<Employee>> employeesDepartment = new HashMap<>();
	@Override
	public ReturnCode addEmployee(Employee empl) {
		if (mapEmployees.containsKey(empl.id)) {
			return ReturnCode.EMPLOYEE_ALREADY_EXISTS;
		}
		Employee emplS = new Employee(empl.id, empl.name, empl.birthDate, empl.salary, empl.department);
		mapEmployees.put(emplS.id, emplS);
		employeesAge.computeIfAbsent(getAge(emplS), k -> new LinkedList<Employee>()).add(emplS);
		employeesSalary.computeIfAbsent(emplS.salary, k -> new LinkedList<Employee>()).add(emplS);
		employeesDepartment.computeIfAbsent(emplS.department, k -> new LinkedList<Employee>()).add(emplS);
		
		return ReturnCode.OK;
	}

	private Integer getAge(Employee emplS) {
		return (int)ChronoUnit.YEARS.between(emplS.birthDate, LocalDate.now());
	}

	@Override
	public ReturnCode removeEmployee(long id) {
		Employee emp = mapEmployees.get(id);
		if(emp==null) {
			return ReturnCode.EMPLOYEE_NOT_FOUND;
		}
		removeEmpSalary(emp);
		removeEmpDepartment(emp);
		removeEmpAge(emp);
		mapEmployees.remove(id);
		return ReturnCode.OK;
	}
		private void removeEmpDepartment(Employee employee) {
			List<Employee> emps = employeesDepartment.get(employee.department);
			emps.remove(employee);
			if(emps.isEmpty()) {
				employeesDepartment.remove(employee.department);
			}
		}
		
		private void removeEmpSalary(Employee employee) {
			List<Employee> emps = employeesSalary.get(employee.salary);
			emps.remove(employee);
			if(emps.isEmpty()) {
				employeesSalary.remove(employee.salary);
			}
		}
		
		private void removeEmpAge(Employee employee) {
			Integer age = getAge(employee);
			List<Employee> emps = employeesAge.get(age);
			emps.remove(employee);
			if(emps.isEmpty()) {
				employeesAge.remove(age);
			}
		}
	@Override
	public Iterable<Employee> getAllEmployees() {
		return getEmployeesFromList(mapEmployees.values().stream().toList());
	}

	@Override
	public Employee getEmployee(long id) {
		return mapEmployees.containsKey(id) ? new Employee(mapEmployees.get(id)) : null;
	}

	@Override
	public Iterable<Employee> getEmployeesByAge(int ageFrom, int ageTo) {
		return joinEmpLists(employeesAge.subMap(ageFrom, true, ageTo, true));
	}
	private Iterable<Employee> joinEmpLists(NavigableMap<Integer, List<Employee>> empSubMap) {
		return getEmployeesFromList(empSubMap.values().stream().flatMap(l -> l.stream()).toList());
	}
	@Override
	public Iterable<Employee> getEmployeesBySalary(int salaryFrom, int salaryTo) {
		return joinEmpLists(employeesSalary.subMap(salaryFrom, true, salaryTo, true));
	}

	@Override
	public Iterable<Employee> getEmployeesByDepartment(String department) {
		List<Employee> employees = employeesDepartment.getOrDefault(department, Collections.emptyList());
		
		return employees.isEmpty() ? employees : getEmployeesFromList(employees);
	}

	private Iterable<Employee> getEmployeesFromList(List<Employee> employees) {
		
		return employees.stream().map(e ->
		new Employee(e.id, e.name, e.birthDate, e.salary, e.department)).toList();
	}

	@Override
	public Iterable<Employee> getEmployeesByDepartmentAndSalary(String department, int salaryFrom, int salaryTo) {
		/* V.R. The variable name PempSallarySet) has to be empSallaryList.
		 *  More better is to write all in the single line. Like follwing:
		 *  return getEmployeesFromList(employeesDepartment.get(department).stream()
		 *  .filter(e -> e.salary >= salaryFrom && e.salary <= salaryTo).toList());
		 */
		List<Employee> empSallarySet = employeesDepartment.get(department).stream()
				.filter(e -> e.salary >= salaryFrom && e.salary <= salaryTo).toList();
		return getEmployeesFromList(empSallarySet);
	}

	@Override
	public ReturnCode updateSalary(long id, int newSalary) {
		if (!mapEmployees.containsKey(id)) {
			return ReturnCode.EMPLOYEE_NOT_FOUND;
		}
		Employee emplToUpdate = mapEmployees.get(id);
		/* V.R.
		 *  What to do if newSalary==emplToUpdate.salary?
		 *  There is the special returned code ==SALARY_NOT_UPDATED
		 */
		removeEmpSalary(emplToUpdate);
		emplToUpdate.salary = newSalary;
		employeesSalary.computeIfAbsent(newSalary, k -> new LinkedList<Employee>()).add(emplToUpdate);
		return ReturnCode.OK;
	}

	@Override
	public ReturnCode updateDepartment(long id, String newDepartment) {
		if (!mapEmployees.containsKey(id)) {
			return ReturnCode.EMPLOYEE_NOT_FOUND;
		}
		Employee emplToUpdate = mapEmployees.get(id);
		/* V.R.
		 *  What to do if newDepartment==emplToUpdate.department?
		 *  There is the special returned code ==DEPARTMENT_NOT_UPDATED
		 */
		removeEmpDepartment(emplToUpdate);
		emplToUpdate.department = newDepartment;
		employeesDepartment.computeIfAbsent(newDepartment, k -> new LinkedList<Employee>()).add(emplToUpdate);
		return ReturnCode.OK;
	}

}