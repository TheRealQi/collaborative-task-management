package com.q.colabtaskmanagement.controller;

import com.q.colabtaskmanagement.common.dto.apiresponse.ApiSuccessResponse;
import com.q.colabtaskmanagement.common.dto.workspace.*;
import com.q.colabtaskmanagement.security.model.CustomUserDetails;
import com.q.colabtaskmanagement.service.interfaces.WorkspaceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/workspaces")
public class WorkspaceController {
    private final WorkspaceService workspaceService;

    @Autowired
    public WorkspaceController(WorkspaceService workspaceService) {
        this.workspaceService = workspaceService;
    }

    @PostMapping("/create")
    public ResponseEntity<ApiSuccessResponse<WorkspaceDTO>> createWorkspace(@Valid @RequestBody WorkspaceCreationDTO workspaceCreationDTO, @AuthenticationPrincipal CustomUserDetails currentUser) {

        WorkspaceDTO workspaceDTO = workspaceService.createWorkspace(workspaceCreationDTO);

        ApiSuccessResponse<WorkspaceDTO> apiResponse = new ApiSuccessResponse<>(true, "Workspace created successfully", workspaceDTO, null);

        return ResponseEntity.status(201).body(apiResponse); // still return 201 Created
    }

    @GetMapping("/{workspaceId}")
    public ResponseEntity<ApiSuccessResponse<WorkspaceDTO>> getWorkspaceById(@PathVariable UUID workspaceId, @AuthenticationPrincipal CustomUserDetails currentUser) {

        WorkspaceDTO workspaceDTO = workspaceService.getWorkspaceById(workspaceId);

        ApiSuccessResponse<WorkspaceDTO> apiResponse = new ApiSuccessResponse<>(true, "Workspace fetched successfully", workspaceDTO, null);

        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping
    public ResponseEntity<ApiSuccessResponse<List<WorkspaceDTO>>> getUserWorkspaces(@AuthenticationPrincipal CustomUserDetails currentUser) {

        List<WorkspaceDTO> workspaceDTO = workspaceService.getAllUserWorkspaces();

        ApiSuccessResponse<List<WorkspaceDTO>> apiResponse = new ApiSuccessResponse<>(true, "Workspaces fetched successfully", workspaceDTO, null);

        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/dashboard")
    public ResponseEntity<ApiSuccessResponse<List<WorkspaceWithBoardsDTO>>> getUserWorkspacesForDashboard(@AuthenticationPrincipal CustomUserDetails currentUser) {

        List<WorkspaceWithBoardsDTO> workspaceDTO = workspaceService.getAllUserWorkspacesWithBoards();

        ApiSuccessResponse<List<WorkspaceWithBoardsDTO>> apiResponse = new ApiSuccessResponse<>(true, "Workspaces fetched successfully", workspaceDTO, null);

        return ResponseEntity.ok(apiResponse);
    }

    @PutMapping("/{workspaceId}")
    public ResponseEntity<ApiSuccessResponse<Void>> editWorkspace(@PathVariable UUID workspaceId, @Valid @RequestBody WorkspaceEditDTO workspaceEditDTO, @AuthenticationPrincipal CustomUserDetails currentUser) {
        workspaceService.editWorkspace(workspaceId, workspaceEditDTO);
        ApiSuccessResponse<Void> apiResponse = new ApiSuccessResponse<>(true, "Workspace edited successfully", null, null);
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping("/{workspaceId}")
    public ResponseEntity<ApiSuccessResponse<Void>> deleteWorkspace(@PathVariable UUID workspaceId, @AuthenticationPrincipal CustomUserDetails currentUser) {
        workspaceService.deleteWorkspaceById(workspaceId);
        ApiSuccessResponse<Void> apiResponse = new ApiSuccessResponse<>(true, "Workspace deleted successfully", null, null);
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/{workspaceId}/members")
    public ResponseEntity<ApiSuccessResponse<List<WorkspaceMemberDTO>>> getWorkspaceMembers(@PathVariable UUID workspaceId, @AuthenticationPrincipal CustomUserDetails currentUser) {

        List<WorkspaceMemberDTO> members = workspaceService.getWorkspaceMembers(workspaceId);

        ApiSuccessResponse<List<WorkspaceMemberDTO>> apiResponse = new ApiSuccessResponse<>(true, "Workspace members fetched successfully", members, null);

        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping("/{workspaceId}/members/{userId}")
    public ResponseEntity<ApiSuccessResponse<Void>> removeWorkspaceMember(@PathVariable UUID workspaceId, @PathVariable UUID userId, @AuthenticationPrincipal CustomUserDetails currentUser) {

        workspaceService.removeMember(workspaceId, userId);

        ApiSuccessResponse<Void> apiResponse = new ApiSuccessResponse<>(true, "Member removed successfully", null, null);

        return ResponseEntity.ok(apiResponse);
    }

    @PutMapping("/{workspaceId}/members/role")
    public ResponseEntity<ApiSuccessResponse<Void>> changeMemberRole(@PathVariable UUID workspaceId, @RequestBody WorkspaceRoleChangeDTO roleChangeDTO, @AuthenticationPrincipal CustomUserDetails currentUser) {

        workspaceService.changeMemberRole(workspaceId, roleChangeDTO);

        ApiSuccessResponse<Void> apiResponse = new ApiSuccessResponse<>(true, "Member role changed successfully", null, null);

        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping("/{workspaceId}/leave")
    public ResponseEntity<ApiSuccessResponse<Void>> leaveWorkspace(@PathVariable UUID workspaceId, @AuthenticationPrincipal CustomUserDetails currentUser) {

        workspaceService.leaveWorkspace(workspaceId);

        ApiSuccessResponse<Void> apiResponse = new ApiSuccessResponse<>(true, "Left workspace successfully", null, null);

        return ResponseEntity.ok(apiResponse);
    }
}
