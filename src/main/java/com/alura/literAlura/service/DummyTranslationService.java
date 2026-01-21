import com.alura.literAlura.service.TranslationService;
import org.springframework.stereotype.Service;

@Service
public class DummyTranslationService implements TranslationService {
    @Override
    public String traducir(String texto, String origen, String destino) {
        return texto;
    }
}
