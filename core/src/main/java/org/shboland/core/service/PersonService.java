package org.shboland.core.service;

import org.shboland.persistence.db.repo.DetailsRepository;
import org.shboland.persistence.db.hibernate.bean.Details;
import java.util.List;
import org.shboland.persistence.criteria.PersonSearchCriteria;
import java.util.Optional;
import org.shboland.persistence.db.hibernate.bean.Person;
import org.shboland.persistence.db.repo.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class PersonService {

    private final PersonRepository personRepository;
    private final DetailsRepository detailsRepository;
    // @FieldInput

    @Autowired
    public PersonService(DetailsRepository detailsRepository, PersonRepository personRepository) {
        this.personRepository = personRepository;
        this.detailsRepository = detailsRepository;
        // @ConstructorInput
    }
    
    // @Input

    public Person fetchPersonForDetails(long detailsId) {
        Optional<Details> detailsOptional = detailsRepository.findById(detailsId);
        return detailsOptional.isPresent() && detailsOptional.get().getPerson() != null ? detailsOptional.get().getPerson() : null;
    }

    public boolean removeDetails(long personId, long detailsId) {
        Optional<Person> personOptional = personRepository.findById(personId);
        if (personOptional.isPresent()) {
            Person person = personOptional.get();

            Optional<Details> detailsOptional = detailsRepository.findById(detailsId);
            if (detailsOptional.isPresent()) {
                Details details = detailsOptional.get();

                if (details.getPerson() != null && person.getId().equals(details.getPerson().getId())) {
    
                    Details newDetails = details.toBuilder()
                            .person(null)
                            .build();
                    detailsRepository.save(newDetails);
                    return true;
                }
            }
        }

        return false;
    }

    public boolean updatePersonWithDetails(long personId, long detailsId) {
        Optional<Person> personOptional = personRepository.findById(personId);
        if (personOptional.isPresent()) {
            Person person = personOptional.get();

            Optional<Details> detailsOptional = detailsRepository.findById(detailsId);
            if (detailsOptional.isPresent()) {

                Details newDetails = detailsOptional.get().toBuilder()
                        .person(person)
                        .build();
                detailsRepository.save(newDetails);
                return true;
            }
        }

        return false;
    }
  
    public int findNumberOfPerson(PersonSearchCriteria sc) {
        return personRepository.findNumberOfPersonBySearchCriteria(sc);
    }
    

    public List<Person> findBySearchCriteria(PersonSearchCriteria sc) {
        return personRepository.findBySearchCriteria(sc);
    }

    public Person save(Person person) {
        return personRepository.save(person);
    }

    public Optional<Person> fetchPerson(long personId) {
        return personRepository.findById(personId);
    }

    public boolean deletePerson(long personId) {
        Optional<Person> person = personRepository.findById(personId);

        if (person.isPresent()) {
            personRepository.delete(person.get());
            return true;
        } else {
            return false;
        }
    }
    
}