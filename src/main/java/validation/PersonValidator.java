/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package validation;

/**
 *
 * @author Samuel
 */
import exceptions.*;
import com.gamezone.model.Person;
import java.util.List;

/** Validation rules for persons. */
public final class PersonValidator {
    private PersonValidator() { }

    /** @throws InvalidDataException if a value is not valid */
    public static void validatePersonData(String id, String name, String email, String phone) {
        if (id == null || id.isBlank()) throw new InvalidDataException("identificación", "no puede estar vacía");
        if (name == null || name.isBlank()) throw new InvalidDataException("nombre", "no puede estar vacío");
        if (phone == null || phone.isBlank()) throw new InvalidDataException("teléfono", "no puede estar vacío");
        if (email == null || !email.contains("@") || !email.contains(".")) {
            throw new InvalidDataException("correo", "debe contener '@' y '.'");
        }
    }

    /** @throws ResourceNotFoundException if the person is null */
    public static void validatePersonExists(Person person, String personId) {
        if (person == null) throw new ResourceNotFoundException("La persona", personId);
    }

    /** @throws BusinessRuleException if the id already exists */
    public static void validateUniquePerson(List<Person> existing, String newId) {
        for (Person p : existing) {
            if (p.getId().equals(newId)) {
                throw new BusinessRuleException("Ya existe una persona con el identificador " + newId);
            }
        }
    }
}
