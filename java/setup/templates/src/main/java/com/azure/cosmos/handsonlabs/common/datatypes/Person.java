// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.cosmos.handsonlabs.common.datatypes;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import com.github.javafaker.Bool;
import com.github.javafaker.Faker;

public class Person {
    private String id;
    private String website;
    private String gender;
    private String firstName;
    private String lastName;
    private String userName;
    private String avatar;
    private String email;
    private Date dateOfBirth;
    private CardAddress address;
    private String phone;
    private CardCompany company;

    public Person(String id, String website, String gender, String firstName, String lastName, String userName,
            String avatar, String email, Date dateOfBirth, CardAddress address, String phone, CardCompany company) {

        this.setId(id);
        this.website = website;
        this.gender = gender;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.avatar = avatar;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.phone = phone;
        this.company = company;
    }

    public Person() {
        this(UUID.randomUUID().toString(),"","","","","","","",new Date(),new CardAddress(),"",new CardCompany());

        Faker faker = new Faker();
        
        // id is already taken care of
        this.website = faker.internet().domainName();
        if (faker.bool().bool()) 
            this.gender = "female";
        else
            this.gender = "male";
        this.firstName = faker.name().firstName();
        this.lastName = faker.name().lastName();
        this.userName = faker.name().username();
        this.avatar = faker.funnyName().name();
        this.email = faker.internet().emailAddress();
        this.dateOfBirth = faker.date().birthday();
        //address is taken care of
        this.phone = faker.phoneNumber().phoneNumber();
        //company is taken care of
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWebsite() {
        return website;
    }

    public CardCompany getCompany() {
        return company;
    }

    public void setCompany(CardCompany company) {
        this.company = company;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public CardAddress getAddress() {
        return address;
    }

    public void setAddress(CardAddress address) {
        this.address = address;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
}