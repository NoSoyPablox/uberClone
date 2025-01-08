package uv.uberEats.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import uv.uberEats.dtos.ComidaResponseDTO;
import uv.uberEats.models.Comida;
import uv.uberEats.models.Establecimiento;
import uv.uberEats.repositories.CalificacionRepository;
import uv.uberEats.repositories.ComidaRepository;
import uv.uberEats.repositories.EstablecimientoRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class ComidaService {

    @Autowired
    ComidaRepository comidaRepository;
    @Autowired
    CalificacionRepository calificacionRepository;
    @Autowired
    EstablecimientoRepository establecimientoRepository;

    // Obtener todas las comidas
    public List<ComidaResponseDTO> obtenerTodasLasComida() {
        List<Comida> comidas = comidaRepository.findAll(Sort.by(Sort.Direction.DESC, "nombre"));
        return mapToComidaResponseDTO(comidas);
    }

    // Obtener comidas por nombre
    public List<ComidaResponseDTO> obtenerComidasPorNombre(String nombre) {
        if (nombre == null || nombre.isEmpty()) {
            return obtenerTodasLasComida();
        }
        List<Comida> comidas = comidaRepository.findComidasByNombre("%" + nombre + "%");
        return mapToComidaResponseDTO(comidas);
    }

    // Obtener comida por ID
    public ComidaResponseDTO obtenerComidaPorId(Integer id) {
        Comida comida = comidaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Comida no encontrada con ID: " + id));
        return mapToComidaResponseDTO(comida);
    }

    // Obtener comidas por establecimiento
    public List<ComidaResponseDTO> obtenerComidasPorEstablecimiento(Integer idEstablecimiento) {
        List<Comida> comidas = comidaRepository.findByEstablecimientoId(idEstablecimiento);
        return mapToComidaResponseDTO(comidas);
    }

    // Método para mapear de Comida a ComidaResponseDTO
    private ComidaResponseDTO mapToComidaResponseDTO(Comida comida) {
        Integer establecimientoId = comida.getEstablecimiento() != null ? comida.getEstablecimiento().getId() : null;
        String establecimientoNombre = comida.getEstablecimiento() != null ? comida.getEstablecimiento().getNombre() : null;

        // Obtener el conteo de calificaciones y el promedio
        Long conteoDeCalificaciones = obtenerConteoDeCalificaciones(comida.getId());
        Double promedioDeCalificaciones = obtenerPromedioDeCalificaciones(comida.getId());

        return new ComidaResponseDTO(
                comida.getId(),
                comida.getNombre(),
                comida.getPrecio(),
                comida.getDescripcion(),
                comida.getImagen(),
                promedioDeCalificaciones,
                conteoDeCalificaciones,
                establecimientoId,
                establecimientoNombre
        );
    }

    private List<ComidaResponseDTO> mapToComidaResponseDTO(List<Comida> comidas) {
        return comidas.stream()
                .map(this::mapToComidaResponseDTO)
                .collect(Collectors.toList());
    }

    //Crear comida
    public ComidaResponseDTO crearComida(String nombre, String precio, String descripcion, Integer establecimientoId, byte[] imagenBytes) {
        // Convertir el precio de String a BigDecimal
        BigDecimal precioDecimal = new BigDecimal(precio);

        // Crear el objeto comida
        Comida nuevaComida = new Comida();
        nuevaComida.setNombre(nombre);
        nuevaComida.setPrecio(precioDecimal);
        nuevaComida.setDescripcion(descripcion);

        // Obtener el establecimiento relacionado
        Establecimiento establecimiento = establecimientoRepository.findById(establecimientoId)
                .orElseThrow(() -> new RuntimeException("Establecimiento no encontrado"));

        nuevaComida.setEstablecimiento(establecimiento); // Asegúrate de configurar correctamente el establecimiento
        nuevaComida.setImagen(imagenBytes); // Establecer la imagen

        // Guardar la comida en la base de datos
        Comida comidaGuardada = comidaRepository.save(nuevaComida);

        // Mapear la comida guardada a un ComidaResponseDTO y devolverlo
        return mapToComidaResponseDTO(comidaGuardada);
    }



    // Eliminar comida por ID
    public void eliminarComida(Integer id) {
        Comida comida = comidaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Comida no encontrada con ID: " + id));
        comidaRepository.delete(comida);
    }

    // Actualizar comida por ID
    public ComidaResponseDTO actualizarComida(Integer id, String nombre, String precio, String descripcion, Integer establecimientoId, byte[] imagenBytes) {
        Comida comidaExistente = comidaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Comida no encontrada con ID: " + id));

        // Actualizar los atributos de la comida existente con los valores proporcionados
        if (nombre != null) {
            comidaExistente.setNombre(nombre);
        }
        if (precio != null) {
            comidaExistente.setPrecio(new BigDecimal(precio));
        }
        if (descripcion != null) {
            comidaExistente.setDescripcion(descripcion);
        }
        if (establecimientoId != null) {
            Establecimiento establecimiento = establecimientoRepository.findById(establecimientoId)
                    .orElseThrow(() -> new RuntimeException("Establecimiento no encontrado"));
            comidaExistente.setEstablecimiento(establecimiento);
        }
        if (imagenBytes != null) {
            comidaExistente.setImagen(imagenBytes);
        }

        // Guardar la comida actualizada
        comidaRepository.save(comidaExistente);

        // Mapear la comida actualizada a un ComidaResponseDTO y devolverlo
        return mapToComidaResponseDTO(comidaExistente);
    }


    // Método para obtener el conteo de calificaciones de una comida
    public Long obtenerConteoDeCalificaciones(Integer comidaId) {
        return calificacionRepository.countByComidaId(comidaId);  // Usamos el método de conteo
    }

    // Método para obtener el promedio de calificaciones de una comida
    public Double obtenerPromedioDeCalificaciones(Integer comidaId) {
        return calificacionRepository.promedioDeCalificaciones(comidaId);  // Usamos el método de promedio
    }


}
