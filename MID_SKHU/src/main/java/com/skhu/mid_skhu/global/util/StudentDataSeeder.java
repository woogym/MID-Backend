package com.skhu.mid_skhu.global.util;

import com.amazonaws.services.ec2.model.SearchTransitGatewayMulticastGroupsRequest;
import com.skhu.mid_skhu.app.entity.interest.InterestCategory;
import com.skhu.mid_skhu.app.entity.student.RoleType;
import com.skhu.mid_skhu.app.entity.student.Student;
import com.skhu.mid_skhu.app.repository.StudentRepository;
import com.skhu.mid_skhu.global.exception.model.CustomException;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import net.datafaker.providers.base.Options;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Profile("test")
@Component
@RequiredArgsConstructor
public class StudentDataSeeder implements CommandLineRunner {

    private final StudentRepository studentRepository;
    private final Random random = new Random();
    private final EntityManager entityManager;

    @Override
    @Transactional(
            propagation = Propagation.REQUIRED,
            rollbackFor = {CustomException.class}
    )
    public void run(String... args) throws Exception {
        int total = 100_000;
        int batchSize = 5000;

        Faker faker = new Faker(new Locale("ko"));
        Options options = faker.options();

        List<Student> buffer = new ArrayList<>(batchSize);
        long start = System.currentTimeMillis();

        for (int i = 0; i < total; i++) {
            String studentNo = String.format("2025%05d", i);

            Student student = Student.builder()
                    .studentNo(studentNo)
                    .password("test1234")
                    .name(faker.name().fullName())
                    .department(faker.job().field())
                    .phoneNumber("010-" + faker.number().digits(4) + "-" + faker.number().digits(4))
                    .fcmToken(UUID.randomUUID().toString())
                    .roleType(RoleType.ROLE_STUDENT)
                    .category(randomCategories(options))
                    .build();

            buffer.add(student);

            if (buffer.size() == batchSize) {
                studentRepository.saveAll(buffer);
                studentRepository.flush();
                entityManager.clear();
                buffer.clear();
                System.out.printf("inserted %d students...\n", i);

                if ((i + 1) % 20000 == 0) {
                    System.gc();
                }
            }
        }

        if (!buffer.isEmpty()) {
            studentRepository.saveAll(buffer);
            studentRepository.flush();
            entityManager.clear();
        }

        long end = System.currentTimeMillis();
        System.out.println("Done inserting 100,000 students");
        System.out.printf("elapsed time: %,d ms\n", (end - start));
    }

    private List<InterestCategory> randomCategories(Options options) {
        InterestCategory[] all = InterestCategory.values();
        Set<InterestCategory> result = new HashSet<>();
        result.add(options.option(all));
        if (random.nextBoolean()) {
            result.add(options.option(all));
        }

        return new ArrayList<>(result);
    }
}
