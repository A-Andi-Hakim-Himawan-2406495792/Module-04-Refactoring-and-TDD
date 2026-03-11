package id.ac.ui.cs.advprog.eshop.repository;
import id.ac.ui.cs.advprog.eshop.model.Car;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class CarRepository {
    static int id = 0;
    private List<Car> carData = new ArrayList<>();

    public Car create(Car car) {
        if(car.getCarId() == null) {
            UUID uuid = UUID.randomUUID();
            car.setCarId(uuid.toString());
        }
        carData.add(car);
        return car;
    }

    public List<Car> findAll(){
        return new ArrayList<>(carData);
    }

    public Optional<Car> findById(String id) {
        for (Car car : carData) {
            if (car.getCarId().equals(id)) {
                return Optional.of(car);
            }
        }
        return Optional.empty();
    }

    public Car update(String id, Car updatedCar) {
        for (Car car : carData) {
            if (car.getCarId().equals(id)) {
                // Update the existing car with the new information
                car.setCarName(updatedCar.getCarName());
                car.setCarColor(updatedCar.getCarColor());
                car.setCarQuantity(updatedCar.getCarQuantity());
                return car;
            }
        }
        return null; // Handle the case where the car is not found
    }

    public void delete(String id) {
        carData.removeIf(car -> car.getCarId().equals(id));
    }
}