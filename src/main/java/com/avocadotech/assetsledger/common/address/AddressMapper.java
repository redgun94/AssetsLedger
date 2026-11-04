package com.avocadotech.assetsledger.common.address;

public class AddressMapper {

    public static Address toEntity(AddressDto addCompanyDto){
        if(addCompanyDto == null){
            return null;
        }
        Address address = new Address();
        address.setStreet(addCompanyDto.street());
        address.setCity(addCompanyDto.city());
        address.setState(addCompanyDto.state());
        address.setZipCode(addCompanyDto.zipCode());
        return address;
    }

    public static AddressDto toAddressResponseDto(Address addressToDto){
        if(addressToDto == null){
            return null;
        }

        return new AddressDto(
                addressToDto.getStreet(),
                addressToDto.getCity(),
                addressToDto.getState(),
                addressToDto.getZipCode()
        );
    }
}
