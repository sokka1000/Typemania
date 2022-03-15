package com.example.lab6.socialnetwork.domain;

import java.text.SimpleDateFormat;
import java.util.Date;


public class Friendship extends Entity<Tuple<Long,Long>> {


    private Long ID1,ID2;
    String date;

    public Friendship(Long ID1, Long ID2) {
        Date date1 = new Date(System.currentTimeMillis());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        this.ID1=ID1;
        this.ID2=ID2;
        this.date= formatter.format(date1);
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Long getID1() {
        return ID1;
    }

    public Long getID2() {
        return ID2;
    }

    /**
     *
     * @return the date when the friendship was created
     */
    public String getDate() {
        return date;
    }

    public String getMonth(){
        String month = "";
        month += date.charAt(5);
        month += date.charAt(6);
        return month;
    }


    @Override
    public String toString() {
        return "Friendship{" +
                "date=" + date +
                ", ID1=" + ID1 +
                ", ID2=" + ID2 +
                ", ID.LEFT=" + this.getId().getLeft()+
                ", ID.RIGHT=" + this.getId().getRight()+
                '}';
    }
}
