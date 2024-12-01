package Interfaces.DAL;

import entity.Firm;
import java.util.List;

public interface FirmDAO {
    void addFirm(Firm firm);
    Firm getFirmById(long id);
    List<Firm> getAllFirms();
    void updateFirm(Firm firm);
    void deleteFirm(long id);

    Firm getFirmByName(String firmName);
    List<Firm> getFirmsByName(String name);
    List<Firm> getFirmsByAddress(String address);
    List<Firm> getFirmsByPhone(String phone);
}
