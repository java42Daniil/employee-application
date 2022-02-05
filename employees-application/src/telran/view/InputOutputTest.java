package telran.view;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import telran.employees.dto.Employee;

class InputOutputTest {
InputOutput io = new ConsoleInputOutput();
	@BeforeEach
	void setUp() throws Exception {
	}

	//@Test
	void employeeInputAsOneString() {
		Employee empl = io.readObject("Enter employee data as string <id>#<name>#<birthdate ISO>#<salary>#<department>",
				"Wrong format of employee data", InputOutputTest::toEmployeeFromStr);
		io.writeObjectLine(empl);
	}
	static Employee toEmployeeFromStr(String str) {
		String emplTokens[] = str.split("#");
		long id = Long.parseLong ( emplTokens[0] );
		String name = emplTokens[1];
		LocalDate birthDate = LocalDate.parse(emplTokens[2]);
		int salary = Integer.parseInt(emplTokens[3]);
		String department = emplTokens[4];
		return new Employee(id, name, birthDate, salary, department);
	}
	
	//@Test 
	void readPredicateTest() {
		String str = io.readStringPredicate("enter any string containing exactly 3 symbols",
				"this is no string containing exactly 3 symbols", s -> s.length() == 3);
		assertEquals(3, str.length());
	}
	@Test
	void employeeBySeparateFields() {
		//TODO
		//enter ID by readLong method
			Long id = io.readLong("Enter Employee ID");
			io.writeObjectLine(id);
		//enter Name by readStringPredicate (only letters with capital first letter)
			String strName =  io.readStringPredicate("Enter name with capital first letter", "this name didnt start with capital first letter", 
			s -> s.matches("[A-Z][a-z]*"));
			io.writeObjectLine(strName);
		//enter birthdate by readDate 
			LocalDate birthday = io.readDate("Write date");
			io.writeObjectLine(birthday);
		//enter salary by readInt(prompt, min, max)
			Integer intInRange = io.readInt("write your salary in range", 2000, 10000);
			io.writeObjectLine(intInRange);
		//enter department by readStringOption specifying possible departments
			String listOfDepartments[] = {"QA","Development"};
			Set<String> departments = new HashSet<>(Arrays.asList(listOfDepartments));
			String department = io.readStringOption(
					"Enter one of the following departments: "+Arrays.toString(listOfDepartments), 
					departments);
			io.writeObjectLine("department="+department);
		
	}

}