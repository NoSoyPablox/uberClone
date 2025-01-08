package uv.uberEats.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import uv.uberEats.dtos.ComidaResponseDTO;
import uv.uberEats.dtos.EstablecimientoComidasResponseDTO;
import uv.uberEats.dtos.EstablecimientoDTO;
import uv.uberEats.dtos.EstablecimientoResponseDTO;
import uv.uberEats.models.Establecimiento;
import uv.uberEats.repositories.EstablecimientoRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class EstablecimientoService {

    @Autowired
    EstablecimientoRepository establecimientoRepository;
    @Autowired
    private ComidaService comidaService;

    // Obtener todos los establecimientos ordenados por nombre Descendente ABC
    public List<EstablecimientoResponseDTO> obtenerTodos() {
        List<Establecimiento> establecimientos = establecimientoRepository.findAll(Sort.by(Sort.Direction.DESC, "nombre"));
        return mapToEstablecimientoResponseDTO(establecimientos);
    }

    // Obtener todos por nombre buscado
    public List<EstablecimientoResponseDTO> obtenerPorNombre(String nombre) {
        if (nombre == null || nombre.isEmpty()) {
            // En caso de cadena vacía, devuelve todos
            return obtenerTodos();
        }
        List<Establecimiento> establecimientos = establecimientoRepository.findEstablecimientoByNombre("%" + nombre + "%");
        return mapToEstablecimientoResponseDTO(establecimientos);
    }

    // Obtener un establecimiento por ID
    public Establecimiento obtenerPorId(Integer id) {
        return establecimientoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Establecimiento no encontrado con ID: " + id));
    }

    public EstablecimientoComidasResponseDTO obtenerEstablecimientoComidasPorId(Integer id) {
        Establecimiento establecimiento = establecimientoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Establecimiento no encontrado con ID: " + id));

        // Obtener las comidas asociadas a este establecimiento
        List<ComidaResponseDTO> comidas = comidaService.obtenerComidasPorEstablecimiento(id);

        // Crear y devolver el DTO
        return new EstablecimientoComidasResponseDTO(
                establecimiento.getId().intValue(),
                establecimiento.getNombre(),
                establecimiento.getCodigoPostal(),
                establecimiento.getNumero(),
                establecimiento.getCalle(),
                establecimiento.getPais(),
                establecimiento.getCiudad(),
                establecimiento.getEstado(),
                establecimiento.getLatitud(),
                establecimiento.getLongitud(),
                comidas
        );
    }

    //Agregar un establecimiento
    public EstablecimientoResponseDTO agregarEstablecimiento(EstablecimientoDTO establecimientoCreateDTO) {
        // Mapea el DTO a la entidad Establecimiento
        Establecimiento establecimiento = new Establecimiento();
        establecimiento.setNombre(establecimientoCreateDTO.getNombre());
        establecimiento.setCodigoPostal(establecimientoCreateDTO.getCodigoPostal());
        establecimiento.setNumero(establecimientoCreateDTO.getNumero());
        establecimiento.setCalle(establecimientoCreateDTO.getCalle());
        establecimiento.setPais(establecimientoCreateDTO.getPais());
        establecimiento.setCiudad(establecimientoCreateDTO.getCiudad());
        establecimiento.setEstado(establecimientoCreateDTO.getEstado());
        establecimiento.setLatitud(establecimientoCreateDTO.getLatitud());
        establecimiento.setLongitud(establecimientoCreateDTO.getLongitud());

        // Guarda el establecimiento en la base de datos
        Establecimiento nuevoEstablecimiento = establecimientoRepository.save(establecimiento);

        // Mapea la entidad Establecimiento a EstablecimientoResponseDTO
        return mapToEstablecimientoResponseDTO(nuevoEstablecimiento);
    }

    //Eliminar un establecimiento
    public Establecimiento eliminarEstablecimiento(Integer id) {
        Establecimiento establecimiento = obtenerPorId(id);
        establecimientoRepository.delete(establecimiento);
        return establecimiento;
    }

    //Modificar un establecimiento
    public EstablecimientoResponseDTO actualizarEstablecimiento(Integer id, EstablecimientoDTO establecimientoDTO) {
        // Verificar si el establecimiento existe
        Establecimiento existente = establecimientoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Establecimiento no encontrado con ID: " + id));

        // Mapear el DTO a la entidad Establecimiento
        existente.setNombre(establecimientoDTO.getNombre());
        existente.setCodigoPostal(establecimientoDTO.getCodigoPostal());
        existente.setNumero(establecimientoDTO.getNumero());
        existente.setCalle(establecimientoDTO.getCalle());
        existente.setPais(establecimientoDTO.getPais());
        existente.setCiudad(establecimientoDTO.getCiudad());
        existente.setEstado(establecimientoDTO.getEstado());
        existente.setLatitud(establecimientoDTO.getLatitud());
        existente.setLongitud(establecimientoDTO.getLongitud());

        // Guardar los cambios en la base de datos
        Establecimiento actualizado = establecimientoRepository.save(existente);

        // Mapear la entidad actualizada a EstablecimientoResponseDTO
        return mapToEstablecimientoResponseDTO(actualizado);
    }


    // Método para mapear de Establecimiento a EstablecimientoResponseDTO
    private EstablecimientoResponseDTO mapToEstablecimientoResponseDTO(Establecimiento establecimiento) {
        return new EstablecimientoResponseDTO(
                establecimiento.getId().intValue(), // Asumimos que el ID es Long, lo convertimos a Integer
                establecimiento.getNombre(),
                establecimiento.getCodigoPostal(),
                establecimiento.getNumero(),
                establecimiento.getCalle(),
                establecimiento.getPais(),
                establecimiento.getCiudad(),
                establecimiento.getEstado(),
                establecimiento.getLatitud(),
                establecimiento.getLongitud()
        );
    }

    // Método para mapear de Lista de Establecimientos a Lista de EstablecimientoResponseDTO
    private List<EstablecimientoResponseDTO> mapToEstablecimientoResponseDTO(List<Establecimiento> establecimientos) {
        return establecimientos.stream()
                .map(this::mapToEstablecimientoResponseDTO)
                .collect(Collectors.toList());
    }
}
