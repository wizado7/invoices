package impl;

import Interfaces.DAL.FirmDAO;
import entity.Firm;

import java.util.List;

public class FirmDAOImpl implements FirmDAO {
    @Override
    public void addFirm(Firm firm) {

    }

    @Override
    public Firm getFirmById(long id) {
        return null;
    }

    @Override
    public List<Firm> getAllFirms() {
        return null;
    }

    @Override
    public void updateFirm(Firm firm) {

    }

    @Override
    public void deleteFirm(long id) {

    }

    @Override
    public Firm getFirmByName(String firmName) {
        return null;
    }

    @Override
    public List<Firm> getFirmsByName(String name) {
        return null;
    }

    @Override
    public List<Firm> getFirmsByAddress(String address) {
        return null;
    }

    @Override
    public List<Firm> getFirmsByPhone(String phone) {
        return null;
    }
}
