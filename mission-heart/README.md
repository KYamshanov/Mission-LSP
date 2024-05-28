Project provides core logic for mission project.
This project should have authorization and subproject launch system.

Basically on 
1) android Launch system will use default Android Inter-Process Communication (IPC) 
2) desktop system will be as plugins mechanic. e.g. Heart connect another jar parts

The main idiom of project is simplify as possible and be nice!


Code convention.

about viewModel:
The project use decompose librarty for navigation and decompose screens to simple components.
If you need public ViewModel (for composable function) you have to (probably it aproach will be deprecated)
1) create public interface of ViewModel (ViewModelApi)
2) in component create class (static class) ViewModel `private class ViewModel : InstanceKeeper.Instance, ViewModelApi`
3) in component create instance of ViewModel `public val viewModel : ViewModelApi = instanceKeeper.getOrCreate(::ViewModel)`

If you need private ViewModel you have to:
1) in component create class (static class) ViewModel `private class SomeRetained : InstanceKeeper.Instance`
2) in component create instance of ViewModel `private val someRetained = instanceKeeper.getOrCreate(::SomeRetained)`

about component implementations:
Use `Default` prefix to mark implementation of Component leads to difficult identification witch component
class implement.\
Use Impl suffix if component have only one implementation. If component has more than one implementations each implementation have to specific prefix\
Implementation of component have to lie at `impl` folder

DI:
at the moment heart-LSP doesn`t use any DI framework. Becouse it contradicts to main idiom.\
We explicit create any instance of class.