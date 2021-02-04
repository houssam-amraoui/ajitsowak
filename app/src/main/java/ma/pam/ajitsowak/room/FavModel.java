package ma.pam.ajitsowak.room;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity()
public class FavModel {


    @PrimaryKey
    int idProduct;

    public FavModel() {

    }

    public int getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }


}
