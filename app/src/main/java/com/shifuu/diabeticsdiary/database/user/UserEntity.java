package com.shifuu.diabeticsdiary.database.user;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = "users")
public class UserEntity implements Serializable {

    public static final long serialVersionUID = 652968509826770L;

    @PrimaryKey (autoGenerate = true)
    @NonNull
    private long id;

    @NonNull
    private String name;

    @NonNull
    private String passwd;

    @ColumnInfo (typeAffinity = ColumnInfo.BLOB)
    private byte[] avatar;
    private double weight;

    public byte[] getAvatar() {
        return avatar;
    }

    public long getId() {
        return id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public String getPasswd() {
        return passwd;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public UserEntity(long id, String name, String passwd, byte[] avatar, double weight)
    {
        this.id = id;
        this.name = name;
        this.passwd = passwd;
        this.avatar = avatar;
        this.weight = weight;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity user = (UserEntity) o;
        return id == user.id && Double.compare(user.weight, weight) == 0 && name.equals(user.name) && passwd.equals(user.passwd) && Arrays.equals(avatar, user.avatar);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, name, passwd, weight);
        result = 31 * result + Arrays.hashCode(avatar);
        return result;
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", passwd='" + passwd + '\'' +
                ", weight=" + weight +
                '}';
    }
}
