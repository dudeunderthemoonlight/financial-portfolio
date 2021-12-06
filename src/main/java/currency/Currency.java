package currency;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public enum Currency {
    USD(840),EUR(978),RUB(0),BYN(933),KZT(398), CNY(156), TRY(949), UAH(980),GBP(826);
    private final int id;







}
