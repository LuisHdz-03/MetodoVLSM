# <img src="https://api.iconify.design/ph:coffee-bold.svg?color=%2338C2FF" height="32" valign="middle"/> MetodoVLSM - Enterprise IP Subnetting Engine

Una implementación de arquitectura de software robusta, fuertemente tipada y orientada a objetos desarrollada en **Java** para automatizar el cálculo, enmascaramiento y la verificación de esquemas de direccionamiento IPv4 utilizando el método **VLSM (Variable Length Subnet Mask)**. El sistema destaca por su apego estricto a las buenas prácticas de diseño de software, encapsulamiento y modularidad.

---

## <img src="https://api.iconify.design/ph:sparkle-bold.svg?color=%2338C2FF" height="28" valign="middle"/> Características Principales

*   **Arquitectura Modular OOP:** Organización limpia en clases desacopladas donde cada entidad de red (`Subnet`) encapsula su propio estado contractual, atributos (hosts, red, máscara, broadcast) y comportamiento matemático.
*   **Ordenamiento Dinámico de Requerimientos:** Motor lógico integrado que procesa colecciones de datos, ordenando las solicitudes de hosts estrictamente de mayor a menor antes de efectuar las particiones binarias de la red principal.
*   **Cero Desperdicio de Direccionamiento (Módulo 100% Eficiente):** Algoritmo de precisión optimizado para calcular prefijos CIDR exactos basados en la ecuación matemática de redes: $2^n - 2 \ge \text{hosts}$.
*   **Reportes Estructurados en Consola:** Generación de reportes limpios, tabulados y con un formateo de alto nivel directo en la terminal de producción para facilitar la lectura de la topología IP.

---

## <img src="https://api.iconify.design/ph:code-bold.svg?color=%2338C2FF" height="28" valign="middle"/> Stack Tecnológico

*   **Lenguaje Core:** Java (Compatible con JDK 11 o superior)
*   **Paradigma:** Programación Orientada a Objetos (POO) / Clean Code
*   **Dominios Computacionales:** Arquitectura de Redes / Subneteo IPv4 / Operaciones Lógicas

---

## <img src="https://api.iconify.design/ph:folder-open-bold.svg?color=%2338C2FF" height="28" valign="middle"/> Estructura del Código Fuente

El repositorio está organizado separando la capa de interacción en consola de la lógica pura del motor matemático:

```text
MetodoVLSM/
├── src/
│   ├── App.java          # Punto de entrada (Main), captura de flujos de consola y renderizado de reportes
│   └── Verificacion.java # Motor algorítmico central encargado del parseo binario y segmentación de subredes
└── bin/                  # Archivos compilados del sistema ejecutable (.class)
