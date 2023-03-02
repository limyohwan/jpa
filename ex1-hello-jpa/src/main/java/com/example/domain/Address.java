package com.example.domain;

import java.util.Objects;

import javax.persistence.Embeddable;

@Embeddable
public class Address {
    private String city;
    private String street;
    private String zipcode;

    public String getCity() {
        return city;
    }
    public String getStreet() {
        return street;
    }
    public String getZipcode() {
        return zipcode;
    }

    private void setCity(String city) {
        this.city = city;
    }
    private void setStreet(String street) {
        this.street = street;
    }
    private void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(obj == null || getClass() != obj.getClass()) return false;
        Address address = (Address) obj;
        return Objects.equals(getCity(), address.getCity()) &&
        Objects.equals(getStreet(), address.getStreet())&&
        Objects.equals(getZipcode(), address.getZipcode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCity(), getStreet(), getZipcode());
    }
}
