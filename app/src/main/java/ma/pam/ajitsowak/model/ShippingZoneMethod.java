package ma.pam.ajitsowak.model;

import java.io.Serializable;

public class ShippingZoneMethod implements Serializable {
    private String id;
    private int instance_id;
    private String title;
    private int order;
    private boolean enabled;
    private String method_id;
    private String method_title;
    private String method_description;
    private ShippingMethodCost cost;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getInstance_id() {
        return instance_id;
    }

    public void setInstance_id(int instance_id) {
        this.instance_id = instance_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getMethod_id() {
        return method_id;
    }

    public void setMethod_id(String method_id) {
        this.method_id = method_id;
    }

    public String getMethod_title() {
        return method_title;
    }

    public void setMethod_title(String method_title) {
        this.method_title = method_title;
    }

    public String getMethod_description() {
        return method_description;
    }

    public void setMethod_description(String method_description) {
        this.method_description = method_description;
    }

    public ShippingMethodCost getCost() {
        return cost;
    }

    public void setCost(ShippingMethodCost cost) {
        this.cost = cost;
    }
}
