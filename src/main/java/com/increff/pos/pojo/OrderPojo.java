package com.increff.pos.pojo;

import java.time.ZonedDateTime;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class OrderPojo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private ZonedDateTime dateTime;

    @Column(nullable = false)
    private String status = "created";

    public void setOrderInvoiced(){
        this.status = "invoiced";
    }

    public boolean isInvoiced(){
        if(this.status.equals("invoiced")){
            return true;
        }
        return false;
    }
}
