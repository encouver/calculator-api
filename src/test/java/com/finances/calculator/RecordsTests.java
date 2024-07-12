package com.finances.calculator;

import com.finances.calculator.entities.Record;
import com.finances.calculator.entities.User;
import com.finances.calculator.repositories.RecordRepository;
import com.finances.calculator.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

/**
 * @author Marcos Ramirez
 */
@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
class RecordsTests {


	@Mock
	private RecordRepository recordRepository;

	@Mock
	private UserRepository userRepository;

	private Record record;

	private User user;

	@BeforeEach
	public void setUp() {

		user = new User();
		user.setUsername("testuser");
		user.setPassword("$2a$10$TfbUh2JM4ymYpuW71d9/Euadd4gpmT5H9slohxvDMRdYr2yXLZHoy");
		user.setStatus("active");

		record = new Record();
		record.setAmount(55.5D);
		record.setUserBalance(100.0D);
		record.setOperationResponse("test");
		record.setUser(user);
	}

	@Test
	public void testCreateRecord() {
		when(userRepository.save(user)).thenReturn(user);
		when(recordRepository.save(record)).thenReturn(record);
		when(recordRepository.findById(record.getId())).thenReturn(Optional.of(record));

		// saving user and record.
		userRepository.save(user);
		recordRepository.save(record);

		// verifying the record is saved.
		Optional<Record> found = recordRepository.findById(record.getId());
		assertThat(found).isPresent();
		assertThat(found.get().getAmount()).isEqualTo(55.5D);
	}

	@Test
	public void testSoftDeleteRecord() {
		when(recordRepository.findById(record.getId())).thenReturn(Optional.of(record));

		// soft deleting
		recordRepository.deleteById(record.getId());

		// verify the repository delete method is called
		verify(recordRepository, times(1)).deleteById(record.getId());

		// verifyng the record is not returned when searching by its id
		when(recordRepository.findById(record.getId())).thenReturn(Optional.empty());
		Optional<Record> found = recordRepository.findById(record.getId());
		assertThat(found).isNotPresent();
	}

	@Test
	public void testSoftDeletedRecordStillExistsInDatabase() {
		record.setId(1L);
		when(recordRepository.findById(record.getId())).thenReturn(Optional.of(record));

		recordRepository.deleteById(record.getId());

		// verifying the record is marked as deleted (soft delete)
		verify(recordRepository, times(1)).deleteById(record.getId());

		// ensuring that the record still exist on db
		when(recordRepository.findById(record.getId())).thenReturn(Optional.of(record));
		Optional<Record> foundDeleted = recordRepository.findById(record.getId());
		assertThat(foundDeleted).isPresent();
	}

}
