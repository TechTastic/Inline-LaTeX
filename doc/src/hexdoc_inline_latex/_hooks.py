from importlib.resources import Package
from typing_extensions import override
from pathlib import Path
from typing import Any
import re

from hexdoc.plugin import (
    HookReturn,
    ModPlugin,
    ModPluginImpl,
    ModPluginWithBook,
    hookimpl,
)

import hexdoc_inline_latex

from .__gradle_version__ import FULL_VERSION, GRADLE_VERSION
from .__version__ import PY_VERSION

def replace_latex(match) -> str:
    delimiter = match.group(1)
    formula = match.group(2)
    match delimiter:
        case ",":
            size = "tiny"
        case "!":
            size = None
        case "+":
            size = "large"
        case _:
            size = "small"
    if not size:
        return f"<img src=\"https://latex.codecogs.com/png.latex?\\dpi{{300}}\\fg{{FFFFFF}}\\\\{formula}\" alt=\"{formula}\">"
    return f"<img src=\"https://latex.codecogs.com/png.latex?\\dpi{{300}}\\fg{{FFFFFF}}\\{size}\\\\{formula}\" alt=\"{formula}\">"


class InlinelatexPlugin(ModPluginImpl):
    @staticmethod
    @hookimpl
    def hexdoc_mod_plugin(branch: str) -> ModPlugin:
        return InlinelatexModPlugin(branch=branch)


class InlinelatexModPlugin(ModPlugin):
    @property
    @override
    def modid(self) -> str:
        return "inlinelatex"

    @property
    @override
    def full_version(self) -> str:
        return FULL_VERSION

    @property
    @override
    def mod_version(self) -> str:
        return GRADLE_VERSION

    @property
    @override
    def plugin_version(self) -> str:
        return PY_VERSION

    @override
    def resource_dirs(self) -> HookReturn[Package]:
        # lazy import because generated may not exist when this file is loaded
        # eg. when generating the contents of generated
        # so we only want to import it if we actually need it
        from ._export import generated

        return generated
    
    @override
    def jinja_template_root(self) -> tuple[Package, str]:
        return hexdoc_inline_latex, "_templates"

    @override
    def post_render_book(self, template_args: dict[str, Any], output_dir: Path) -> None:
        """Called once per language, after all book files for that language are rendered."""
        site_path = str(output_dir / "index.html")
        with open(site_path, "r", errors="ignore") as f:
            site = f.read()
        if site:
            site = re.sub(r'\[(?:tex|latex|formula)([:;,!+])([^\]]+)\]', replace_latex, site)
            with open(site_path, "w") as f:
                f.write(site)