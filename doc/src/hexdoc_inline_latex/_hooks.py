from importlib.resources import Package
from typing_extensions import override
import os
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
        return hexdoc_inlinelatex, "_templates"

    def replace_latex(match) -> str:
		full = match.group()
		delimiter = match.group(1)
		formula = match.group(2)
		size = match delimiter:
			case ",":
				"\\tiny"
			case "!":
				"\\large"
			case "+":
				"\\LARGE"
			case _:
				"\\small"
		return f"<img src=\"https://latex.codecogs.com/png.latex?\\dpi{{300}}\\fg{{FFFFFF}}\\{size}\\\\{formula}\" alt=\"{full}\">"

    @override
    def post_render_book(self, template_args: dict[str, Any], output_dir: Path) -> None:
		"""Called once per language, after all book files for that language are rendered."""
		site = open(output_dir + "/index.html", "r").read()
		site = re.sub(r'\[(?:tex|latex|formula)([:;,!+])([^]]+)\]', replace_latex, site)
		f = open(output_dir + "/index.html", "x")
		f.write(site)
		f.close()
