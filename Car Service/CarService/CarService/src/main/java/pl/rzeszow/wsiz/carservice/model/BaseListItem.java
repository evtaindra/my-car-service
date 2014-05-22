package pl.rzeszow.wsiz.carservice.model;

/**
 * Created by rsavk_000 on 5/22/2014.
 */
public abstract class BaseListItem {
    public abstract int getContactCount();
    public abstract BaseListItem getContact(int position);
    public abstract int getId();
}
