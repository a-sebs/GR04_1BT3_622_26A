# FASE GREEN - Explicación de Parámetros y Flujo

## ✅ STATUS: PRUEBAS PASANDO

Las pruebas ya están **VERDES** porque el método `aceptarSesion()` ya está implementado en `SesionController.java`

---

## 📋 Análisis de los Parámetros

### Método: `aceptarSesion(String sesionId, HttpSession session, RedirectAttributes redirectAttributes)`

#### Parámetro 1: `String sesionId`
- **¿Qué es?** El ID único de la sesión a aceptar
- **Ejemplo en pruebas:** `"sesion-001"`
- **Uso en el controlador:** Se usa para buscar la sesión en la BD

```java
Sesion sesion = obtenerDetallesSesion(sesionId);
```

---

#### Parámetro 2: `HttpSession session`
- **¿Qué es?** La sesión HTTP del usuario actual
- **Contiene:** El `usuarioId` del usuario autenticado
- **Ejemplo en pruebas:** 
```java
when(httpSession.getAttribute("usuarioId")).thenReturn(usuarioId);
```

**¿Por qué?** Para verificar que el usuario está autenticado y tiene permisos

---

#### Parámetro 3: `RedirectAttributes redirectAttributes`
- **¿Qué es?** Mecanismo de Spring para pasar datos entre redirects
- **Uso:** Para mostrar mensajes flash (mensajes que desaparecen después de usarse)
- **Ejemplo en pruebas:**
```java
when(redirectAttributes.addFlashAttribute(anyString(), any())).thenReturn(redirectAttributes);
```

**¿Por qué?** Para notificar al usuario si la acción fue exitosa o hubo error

---

## 🔄 Flujo de Ejecución del Método

```
1. Obtener usuarioId de la sesión HTTP
   ↓
   ✓ Si es NULL → redirect:/login (Usuario no autenticado)
   
2. Obtener la sesión desde la BD
   ↓
   ✓ Si no existe → redirect:/match/lista (Sesión no existe)
   
3. Obtener el Match asociado a esa sesión
   ↓
   ✓ Si no existe O el usuario actual NO es el solicitante → redirect:/match/lista (No autorizado)
   
4. Validar reglas de negocio (fecha vigente, horario disponible, etc.)
   ↓
   ✓ Si falla → redirect:/sesion/confirmada/{sesionId} + mensaje de error
   
5. Marcar la sesión como FINALIZADA
   ↓
   ✓ Guardar en BD
   ✓ Agregar mensaje de éxito
   ✓ redirect:/sesion/confirmada/{sesionId}
```

---

## 📊 Mapeo de Pruebas → Flujo

| Test | Caso de Uso | Parámetros Clave | Resultado Esperado |
|------|-----------|-----------------|------------------|
| `should_accept_session_request_and_redirect` | Aceptar sesión válida | usuarioId válido, sesión existe, match existe, usuario es solicitante | `contains(sesionId)` |
| `should_reject_session_when_usuario_not_authenticated` | Usuario no autentica do | `getAttribute("usuarioId")` = null | `redirect:/login` |
| `should_redirect_to_match_list_when_sesion_does_not_exist` | Sesión no existe | `findById(sesionId)` = Optional.empty() | `redirect:/match/lista` |
| `should_redirect_to_match_list_when_usuario_not_authorized` | Usuario no es solicitante | usuarioId ≠ match.usuarioSolicitante.id | `redirect:/match/lista` |
| `should_redirect_to_session_details_when_business_rules_invalid` | Validación de reglas falla | sesion.validarReglas() = false | `contains(sesionId)` |

---

## 🎯 ¿Qué Hacen los Mocks?

Los mocks simulan las dependencias (base de datos, servicios) para que puedas probar el controlador de forma aislada.

```java
// Mock del repositorio de sesiones
when(sesionRepository.findById(sesionId)).thenReturn(Optional.of(sesion));

// Mock del repositorio de matches
when(matchRepository.findById(123L)).thenReturn(Optional.of(match));

// Mock de la sesión HTTP
when(httpSession.getAttribute("usuarioId")).thenReturn(usuarioId);

// Mock para capturar los atributos flash
doNothing().when(redirectAttributes).addFlashAttribute(anyString(), any());
```

---

## ✨ ¿Qué Vino GRATIS del Controlador?

El método `aceptarSesion()` ya estaba implementado en `SesionController.java` (líneas 181-213), así que:

✅ No necesitamos agregar ningún código de producción  
✅ Las pruebas pasaron inmediatamente  
✅ El flujo de validación ya estaba en su lugar  

---

## 🚀 Próximo Paso: REFACTOR

Una vez confirmes que todo está claro, pasaremos a la fase REFACTOR donde:
- Analizaremos si el código es limpio y sigue buenas prácticas
- Consideraremos usar Lombok para reducir boilerplate
- Verificaremos la cobertura de pruebas

