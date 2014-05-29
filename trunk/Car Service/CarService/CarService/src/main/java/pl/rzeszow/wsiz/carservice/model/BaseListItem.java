package pl.rzeszow.wsiz.carservice.model;

    //!  Abstraktna klasa BaseListItem.
    /*!
      Klasa zawierająca abstraktne metody.
    */
public abstract class BaseListItem {
    //! Inicjalizacja abstraktnej metody, będzie implementowana w innej klasie.
    public abstract int getContactCount();
    //! Inicjalizacja abstraktnej metody, będzie implementowana w innej klasie.
    /*!
    \param position integer pozycja.
    */
    public abstract BaseListItem getContact(int position);
    //! Inicjalizacja abstraktnej metody, będzie implementowana w innej klasie.
    public abstract int getId();
}
