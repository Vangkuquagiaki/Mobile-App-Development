package NguyenMinhThien.authapp.service.impl;

import NguyenMinhThien.authapp.entity.Country;
import NguyenMinhThien.authapp.repository.CountryRepository;
import NguyenMinhThien.authapp.service.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CountryServiceImpl implements CountryService {
    private final CountryRepository countryRepository;

    @Override
    public List<Country> getAllCountries() {
        return countryRepository.findAll();
    }

    @Override
    public Optional<Country> getCountryById(Integer id) {
        return countryRepository.findById(id);
    }

    @Override
    public Country saveCountry(Country country) {
        return countryRepository.save(country);
    }

    @Override
    public Country updateCountry(Country country) {
        return countryRepository.save(country);
    }

    @Override
    public void deleteCountry(Integer id) {
        countryRepository.deleteById(id);
    }
}
