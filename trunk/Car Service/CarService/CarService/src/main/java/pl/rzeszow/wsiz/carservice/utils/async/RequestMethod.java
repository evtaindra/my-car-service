package pl.rzeszow.wsiz.carservice.utils.async;

/**
 * Zbiór możliwych metod żądania
 * <p>
 *     GET i POST standardowe http żądania
 *     GET_FROM_URL odbieranie beżposrednio za linkiem
 * </p>
 */
public enum RequestMethod {
    POST,
    GET_FROM_URL,
    GET
}
